import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
import matplotlib.animation as animation
import math
from typing import List, Tuple, Any, Optional
from numpy.typing import NDArray
from matplotlib.figure import Figure
from matplotlib.lines import Line2D
from matplotlib.text import Text
from mpl_toolkits.mplot3d.art3d import Poly3DCollection
import collections

G: float = 6.67430e-11
VENUS_ORBIT_RADIUS: float = 1.082e11
TIME_STEP: float = 3600 * 6
TRAJECTORY_BUFFER_SIZE: int = 1000
AXIS_LIMIT: float = VENUS_ORBIT_RADIUS * 1.3


def calculate_gravity(body1: "SpaceBody", body2: "SpaceBody") -> NDArray[np.float64]:
    """Calculates the gravitational force exerted by body2 on body1."""
    r_vec: NDArray[np.float64] = body2.position - body1.position
    r_mag: float = np.linalg.norm(r_vec)
    if r_mag == 0:
        return np.zeros(3, dtype=np.float64)
    force_mag: float = G * body1.mass * body2.mass / r_mag**2
    force_vec: NDArray[np.float64] = force_mag * r_vec / r_mag
    return force_vec


def rotate_y(points: NDArray[np.float64], angle: float) -> NDArray[np.float64]:
    """Rotates points around the y-axis."""
    cos_a: float = np.cos(angle)
    sin_a: float = np.sin(angle)
    rotation_matrix: NDArray[np.float64] = np.array(
        [[cos_a, 0, sin_a], [0, 1, 0], [-sin_a, 0, cos_a]]
    )
    if points.ndim == 1:
        points = points.reshape(1, -1)
    elif points.ndim != 2 or points.shape[1] != 3:
        raise ValueError("Input points must be an (N, 3) array")
    return np.dot(points, rotation_matrix.T)


class SpaceBody:
    def __init__(
        self,
        name: str,
        mass: float,
        radius: float,
        position: NDArray[np.float64],
        velocity: NDArray[np.float64],
        rotation_period: float,
        color: str,
        display_radius_factor: float,
        sphere_resolution: int = 30,
    ):
        self.name: str = name
        self.mass: float = mass
        self.radius: float = radius
        self.position: NDArray[np.float64] = position.copy()
        self.velocity: NDArray[np.float64] = velocity.copy()
        self.rotation_period: float = rotation_period
        self.color: str = color
        self.display_radius: float = VENUS_ORBIT_RADIUS * display_radius_factor
        self.sphere_resolution: int = sphere_resolution

        self.angle: float = 0.0
        if rotation_period != 0:
            self.omega: float = 2 * np.pi / rotation_period
        else:
            self.omega = 0.0


        self.trajectory = collections.deque(maxlen=TRAJECTORY_BUFFER_SIZE)
        self.trajectory.append(self.position.copy())


        u = np.linspace(0, 2 * np.pi, self.sphere_resolution)
        v = np.linspace(0, np.pi, self.sphere_resolution)
        self.base_x: NDArray[np.float64] = self.display_radius * np.outer(
            np.cos(u), np.sin(v)
        )
        self.base_y: NDArray[np.float64] = self.display_radius * np.outer(
            np.sin(u), np.sin(v)
        )
        self.base_z: NDArray[np.float64] = self.display_radius * np.outer(
            np.ones(np.size(u)), np.cos(v)
        )
        self.base_points_rel: NDArray[np.float64] = np.stack(
            [self.base_x.flatten(), self.base_y.flatten(), self.base_z.flatten()],
            axis=-1,
        )


        self.plot_object: Optional[Poly3DCollection] = None

    def update_physics(self, force: NDArray[np.float64], dt: float):
        """Updates velocity and position based on force and time step."""
        if self.mass == 0:
            return

        acceleration: NDArray[np.float64] = force / self.mass
        self.velocity += acceleration * dt
        self.position += self.velocity * dt
        self.trajectory.append(self.position.copy())

    def update_rotation(self, dt: float):
        """Updates the internal rotation angle."""
        self.angle += self.omega * dt

    def update_plot(self, ax: Any):
        """Updates the body's visual representation on the axes."""

        if self.plot_object:
            self.plot_object.remove()


        rotated_points: NDArray[np.float64] = rotate_y(self.base_points_rel, self.angle)


        plot_x: NDArray[np.float64] = (
            rotated_points[:, 0].reshape(self.base_x.shape) + self.position[0]
        )
        plot_y: NDArray[np.float64] = (
            rotated_points[:, 1].reshape(self.base_y.shape) + self.position[1]
        )
        plot_z: NDArray[np.float64] = (
            rotated_points[:, 2].reshape(self.base_z.shape) + self.position[2]
        )


        stride = max(1, self.sphere_resolution // 10)
        self.plot_object = ax.plot_surface(
            plot_x,
            plot_y,
            plot_z,
            color=self.color,
            rstride=stride,
            cstride=stride,
            linewidth=0,
        )

    def update_trajectory_plot(self, line: Line2D):
        """Updates the provided Line2D object with the body's trajectory."""
        traj_array = np.array(self.trajectory)
        if traj_array.shape[0] > 1:
            line.set_data(traj_array[:, 0], traj_array[:, 1])
            line.set_3d_properties(traj_array[:, 2])
        else:
            line.set_data([], [])
            line.set_3d_properties([])


SUN_MASS: float = 1.989e30
VENUS_MASS: float = 4.867e24
SUN_ROTATION_PERIOD: float = 25.38 * 24 * 3600
VENUS_ROTATION_PERIOD: float = -243.02 * 24 * 3600
SUN_RADIUS: float = 6.9634e8
VENUS_RADIUS: float = 6.0518e6

sun = SpaceBody(
    name="Sun",
    mass=SUN_MASS,
    radius=SUN_RADIUS,
    position=np.array([0.0, 0.0, 0.0]),
    velocity=np.array([0.0, 0.0, 0.0]),
    rotation_period=SUN_ROTATION_PERIOD,
    color="yellow",
    display_radius_factor=0.10,
)

venus_initial_v_mag: float = math.sqrt(G * SUN_MASS / VENUS_ORBIT_RADIUS)
venus_initial_pos: NDArray[np.float64] = np.array([VENUS_ORBIT_RADIUS, 0.0, 0.0])
venus_initial_vel: NDArray[np.float64] = np.array([0.0, venus_initial_v_mag, 0.0])

venus = SpaceBody(
    name="Venus",
    mass=VENUS_MASS,
    radius=VENUS_RADIUS,
    position=venus_initial_pos,
    velocity=venus_initial_vel,
    rotation_period=VENUS_ROTATION_PERIOD,
    color="orange",
    display_radius_factor=0.04,
)

time_elapsed: float = 0.0
simulated_period_found: bool = False
simulated_orbital_period: float = 0.0
initial_venus_angle_rad: float = np.arctan2(venus.position[1], venus.position[0])
total_angle_swept: float = 0.0
last_venus_angle_rad: float = initial_venus_angle_rad
frame_count: int = 0

fig: Figure = plt.figure(figsize=(10, 10))
ax: Any = fig.add_subplot(111, projection="3d")
ax.set_facecolor("black")
ax.grid(False)

trajectory_line: Line2D
(trajectory_line,) = ax.plot(
    [], [], [], "c-", lw=1, label=f"Venus Orbit (Last {TRAJECTORY_BUFFER_SIZE} points)"
)

day_text: Text = ax.text2D(0.02, 0.95, "", color="white", transform=ax.transAxes)

sun.update_plot(ax)
venus.update_plot(ax)


def update(
    frame: int,
) -> Tuple[Optional[Poly3DCollection], Optional[Poly3DCollection], Line2D, Text]:
    global time_elapsed, simulated_period_found, simulated_orbital_period
    global total_angle_swept, last_venus_angle_rad, frame_count



    force_on_venus = calculate_gravity(venus, sun)




    force_on_sun = -force_on_venus




    sun.update_physics(force_on_sun, TIME_STEP)
    venus.update_physics(force_on_venus, TIME_STEP)


    sun.update_rotation(TIME_STEP)
    venus.update_rotation(TIME_STEP)


    if not simulated_period_found and frame > 0:
        current_venus_angle_rad: float = np.arctan2(
            venus.position[1], venus.position[0]
        )
        delta_angle: float = current_venus_angle_rad - last_venus_angle_rad

        if delta_angle > np.pi:
            delta_angle -= 2 * np.pi
        elif delta_angle < -np.pi:
            delta_angle += 2 * np.pi

        total_angle_swept += delta_angle
        last_venus_angle_rad = current_venus_angle_rad

        if total_angle_swept >= 2 * np.pi:
            simulated_orbital_period = time_elapsed + TIME_STEP
            simulated_period_found = True
            print(f"Simulated orbital period found around frame {frame + 1}")
            print(
                f"Simulated Period: {simulated_orbital_period / (24 * 3600):.2f} Earth days"
            )


    sun.update_plot(ax)
    venus.update_plot(ax)
    venus.update_trajectory_plot(trajectory_line)


    time_elapsed += TIME_STEP
    current_day = time_elapsed / (24 * 3600)
    day_text.set_text(f"Day: {current_day:.1f}")

    title_text: str = f"Sun-Venus System"
    if simulated_period_found:
        title_text += (
            f"\nSim. Period: {simulated_orbital_period / (24 * 3600):.1f} Days"
        )
    ax.set_title(title_text, color="white")


    frame_count += 1
    if frame_count % 100 == 0:
        print(f"Frame: {frame_count}, Sim Time: {time_elapsed / (24 * 3600):.1f} days")


    return sun.plot_object, venus.plot_object, trajectory_line, day_text


a: float = VENUS_ORBIT_RADIUS
M_total: float = sun.mass + venus.mass
theoretical_period_squared: float = (4 * np.pi**2 * a**3) / (G * M_total)
theoretical_period: float = np.sqrt(theoretical_period_squared)

print("--- Orbital Period Calculations ---")
print(
    f"Theoretical Period (Kepler's 3rd Law): {theoretical_period / (24 * 3600):.2f} Earth days"
)
print("(Starting infinite simulation - close plot window to stop)")

ax.set_xlim([-AXIS_LIMIT, AXIS_LIMIT])
ax.set_ylim([-AXIS_LIMIT, AXIS_LIMIT])
ax.set_zlim([-AXIS_LIMIT, AXIS_LIMIT])
ax.set_xlabel("X (m)")
ax.set_ylabel("Y (m)")
ax.set_zlabel("Z (m)")
ax.legend()
ax.set_aspect("equal", adjustable="box")

ani: animation.FuncAnimation = animation.FuncAnimation(
    fig, update, interval=20, blit=False, cache_frame_data=False
)

plt.show()

print("\n--- Simulation Stopped ---")
if simulated_period_found:
    print(
        f"Last Calculated Simulated Period: {simulated_orbital_period / (24 * 3600):.2f} Earth days"
    )
print(f"Theoretical Period: {theoretical_period / (24 * 3600):.2f} Earth days")
