use raylib::prelude::*;
use std::f32::consts::PI;

const WIDTH: i32 = 800;
const HEIGHT: i32 = 600;
const BG_COLOR: Color = Color::WHITE;
const COLOR_1: Color = Color::RED;
const COLOR_2: Color = Color::BLUE;

#[derive(Clone, Copy, Debug)]
struct Vector2D {
    x: f32,
    y: f32,
}

impl Vector2D {
    fn new(x: f32, y: f32) -> Self {
        Vector2D { x, y }
    }

    fn add(&self, other: &Vector2D) -> Vector2D {
        Vector2D::new(self.x + other.x, self.y + other.y)
    }

    fn sub(&self, other: &Vector2D) -> Vector2D {
        Vector2D::new(self.x - other.x, self.y - other.y)
    }

    fn scale(&self, scalar: f32) -> Vector2D {
        Vector2D::new(self.x * scalar, self.y * scalar)
    }

    fn length(&self) -> f32 {
        (self.x * self.x + self.y * self.y).sqrt()
    }

    fn normalize(&self) -> Vector2D {
        let len = self.length();
        if len != 0.0 {
            self.scale(1.0 / len)
        } else {
            *self
        }
    }

    fn dot(&self, other: &Vector2D) -> f32 {
        self.x * other.x + self.y * other.y
    }
}

// Структура для движущегося шара
struct MovingCircle {
    pos: Vector2D,
    vel: Vector2D,
    mass: f32,
    radius: f32,
    color: Color,
}

impl MovingCircle {
    fn new(
        pos_x: f32,
        pos_y: f32,
        mass: f32,
        vel_x: f32,
        vel_y: f32,
        radius: f32,
        color: Color,
    ) -> Self {
        MovingCircle {
            pos: Vector2D::new(pos_x, pos_y),
            vel: Vector2D::new(vel_x, vel_y),
            mass,
            radius,
            color,
        }
    }

    fn update_position(&mut self) {
        self.pos = self.pos.add(&self.vel);
        self.bounce_if_needed();
    }

    fn bounce_if_needed(&mut self) {
        if self.pos.x - self.radius < 0.0 {
            self.pos.x = self.radius;
            self.vel.x *= -1.0;
        } else if self.pos.x + self.radius > WIDTH as f32 {
            self.pos.x = WIDTH as f32 - self.radius;
            self.vel.x *= -1.0;
        }

        if self.pos.y - self.radius < 0.0 {
            self.pos.y = self.radius;
            self.vel.y *= -1.0;
        } else if self.pos.y + self.radius > HEIGHT as f32 {
            self.pos.y = HEIGHT as f32 - self.radius;
            self.vel.y *= -1.0;
        }
    }

    fn render(&self, d: &mut RaylibDrawHandle) {
        d.draw_circle(
            self.pos.x as i32,
            self.pos.y as i32,
            self.radius,
            self.color,
        );
    }
}

fn handle_collision(rl: &RaylibHandle, obj1: &mut MovingCircle, obj2: &mut MovingCircle) {
    let delta = obj2.pos.sub(&obj1.pos);
    let dist = delta.length();

    if dist == 0.0 {
        return;
    }

    if dist > obj1.radius + obj2.radius {
        return;
    }

    let normal = delta.normalize();
    let relative_velocity = obj2.vel.sub(&obj1.vel);
    let speed_along_normal = relative_velocity.dot(&normal);

    if speed_along_normal > 0.0 {
        return;
    }

    let impulse_mag = (2.0 * speed_along_normal) / (obj1.mass + obj2.mass);

    obj1.vel = obj1.vel.add(&normal.scale(impulse_mag * obj2.mass));
    obj2.vel = obj2.vel.sub(&normal.scale(impulse_mag * obj1.mass));

    let overlap = (obj1.radius + obj2.radius - dist) / 2.0;
    obj1.pos = obj1.pos.sub(&normal.scale(overlap));
    obj2.pos = obj2.pos.add(&normal.scale(overlap));

    let angle1 = obj1.vel.y.atan2(obj1.vel.x) * 180.0 / PI;
    let angle2 = obj2.vel.y.atan2(obj2.vel.x) * 180.0 / PI;

    rl.trace_log(TraceLogLevel::LOG_INFO, "После столкновения:");
    rl.trace_log(
        TraceLogLevel::LOG_INFO,
        &format!(
            "Красный шар: скорость = ({:.2}, {:.2}), угол = {:.1}°",
            obj1.vel.x, obj1.vel.y, angle1
        ),
    );
    rl.trace_log(
        TraceLogLevel::LOG_INFO,
        &format!(
            "Синий шар: скорость = ({:.2}, {:.2}), угол = {:.1}°",
            obj2.vel.x, obj2.vel.y, angle2
        ),
    );
}

fn main() {
    let (mut rl, thread) = raylib::init()
        .size(WIDTH, HEIGHT)
        .title("Движущиеся шары")
        .build();

    let mut circle_a = MovingCircle::new(101.0, 300.0, 20.0, 10.0, 0.0, 50.0, COLOR_1);
    let mut circle_b = MovingCircle::new(699.0, 300.0, 10.0, 0.0, 0.0, 50.0, COLOR_2);

    rl.set_target_fps(60);

    while !rl.window_should_close() {
        circle_a.update_position();
        circle_b.update_position();
        handle_collision(&rl, &mut circle_a, &mut circle_b);

        let mut d = rl.begin_drawing(&thread);
        d.clear_background(BG_COLOR);
        circle_a.render(&mut d);
        circle_b.render(&mut d);
    }
}
