package main

import (
	"math"
	"strconv"

	gui "github.com/gen2brain/raylib-go/raygui"
	rl "github.com/gen2brain/raylib-go/raylib"
)

const (
	Width    = 800
	Height   = 600
	K        = 10.0
	MaxBalls = 128
)

type Vector2D struct {
	X, Y float32
}

func (v Vector2D) Add(o Vector2D) Vector2D {
	return Vector2D{v.X + o.X, v.Y + o.Y}
}
func (v Vector2D) Sub(o Vector2D) Vector2D {
	return Vector2D{v.X - o.X, v.Y - o.Y}
}
func (v Vector2D) Scale(s float32) Vector2D {
	return Vector2D{v.X * s, v.Y * s}
}
func (v Vector2D) Length() float32 {
	return float32(math.Hypot(float64(v.X), float64(v.Y)))
}
func (v Vector2D) Normalize() Vector2D {
	l := v.Length()
	if l == 0 {
		return v
	}
	return v.Scale(1 / l)
}
func (v Vector2D) Dot(o Vector2D) float32 {
	return v.X*o.X + v.Y*o.Y
}

type MovingCircle struct {
	Pos, Vel Vector2D
	Mass     float32
	Radius   float32
	Color    rl.Color
}

func radiusFromMass(mass float32) float32 {
	return K * float32(math.Sqrt(float64(mass)))
}

func NewMovingCircle(x, y, mass, vx, vy float32, color rl.Color) *MovingCircle {
	return &MovingCircle{
		Pos:    Vector2D{x, y},
		Vel:    Vector2D{vx, vy},
		Mass:   mass,
		Radius: radiusFromMass(mass),
		Color:  color,
	}
}

func (c *MovingCircle) UpdatePosition(formHeight float32) {
	c.Pos = c.Pos.Add(c.Vel)
	c.BounceIfNeeded(formHeight)
}

func (c *MovingCircle) BounceIfNeeded(formHeight float32) {
	if c.Pos.X-c.Radius < 0 {
		c.Pos.X = c.Radius
		c.Vel.X *= -1
	} else if c.Pos.X+c.Radius > Width {
		c.Pos.X = Width - c.Radius
		c.Vel.X *= -1
	}
	if c.Pos.Y-c.Radius < formHeight {
		c.Pos.Y = formHeight + c.Radius
		c.Vel.Y *= -1
	} else if c.Pos.Y+c.Radius > Height {
		c.Pos.Y = Height - c.Radius
		c.Vel.Y *= -1
	}
}

func (c *MovingCircle) Render() {
	rl.DrawCircle(int32(c.Pos.X), int32(c.Pos.Y), c.Radius, c.Color)
}

func handleCollision(a, b *MovingCircle) {
	delta := b.Pos.Sub(a.Pos)
	dist := delta.Length()
	if dist == 0 || dist > a.Radius+b.Radius {
		return
	}
	normal := delta.Normalize()
	relVel := b.Vel.Sub(a.Vel)
	speedAlongNormal := relVel.Dot(normal)
	if speedAlongNormal > 0 {
		return
	}
	impulseMag := (2 * speedAlongNormal) / (a.Mass + b.Mass)
	a.Vel = a.Vel.Add(normal.Scale(impulseMag * b.Mass))
	b.Vel = b.Vel.Sub(normal.Scale(impulseMag * a.Mass))
	overlap := (a.Radius + b.Radius - dist) / 2
	a.Pos = a.Pos.Sub(normal.Scale(overlap))
	b.Pos = b.Pos.Add(normal.Scale(overlap))
}

func clampf(val, minf, maxf float32) float32 {
	if val < minf {
		return minf
	}
	if val > maxf {
		return maxf
	}
	return val
}

func colorNamesString(names []string) string {
	s := ""
	for i, n := range names {
		if i > 0 {
			s += ";"
		}
		s += n
	}
	return s
}

func drawVelocityVector(c *MovingCircle, color rl.Color) {
	// Draw velocity vector from center of ball
	scale := float32(64) // Adjust for visibility
	endX := c.Pos.X + c.Vel.X*scale
	endY := c.Pos.Y + c.Vel.Y*scale
	rl.DrawLineEx(
		rl.NewVector2(c.Pos.X, c.Pos.Y),
		rl.NewVector2(endX, endY),
		2,
		color,
	)

	// Draw angle text (relative to y axis)
	// atan2 returns angle from x axis, so subtract from 90 deg (pi/2) to get from y axis
	angleRad := math.Atan2(float64(c.Vel.Y), float64(c.Vel.X))
	angleFromY := (math.Pi/2 - angleRad) * 180 / math.Pi
	angleFromY = math.Ceil(angleFromY)
	angleStr := strconv.FormatFloat(angleFromY, 'f', 1, 64) + "°"

	// Place text a bit away from the end of the vector
	textX := endX + 8
	textY := endY + 8
	rl.DrawText(angleStr, int32(textX), int32(textY), 16, color)
}

// --- Helper functions for main loop ---

func handleMouseFocus(mouse rl.Vector2, rects []rl.Rectangle, rectColorExpanded rl.Rectangle, focused *int32) {
	if rl.IsMouseButtonPressed(rl.MouseLeftButton) {
		switch {
		case rl.CheckCollisionPointRec(mouse, rects[0]):
			*focused = 1
		case rl.CheckCollisionPointRec(mouse, rects[1]):
			*focused = 2
		case rl.CheckCollisionPointRec(mouse, rects[2]):
			*focused = 3
		case rl.CheckCollisionPointRec(mouse, rects[3]):
			*focused = 4
		case rl.CheckCollisionPointRec(mouse, rects[4]):
			*focused = 5
		case rl.CheckCollisionPointRec(mouse, rects[5]):
			*focused = 6
		default:
			if !rl.CheckCollisionPointRec(mouse, rectColorExpanded) {
				*focused = 0
			}
		}
	}
}

func drawFormLabels(xX, yX, mX, vxX, vyX, colorX, inputW, colorW, labelY float32, labelFontSize int32) {
	rl.DrawText("X", int32(xX+inputW/2)-rl.MeasureText("X", labelFontSize)/2, int32(labelY), labelFontSize, rl.Black)
	rl.DrawText("Y", int32(yX+inputW/2)-rl.MeasureText("Y", labelFontSize)/2, int32(labelY), labelFontSize, rl.Black)
	rl.DrawText("M", int32(mX+inputW/2)-rl.MeasureText("M", labelFontSize)/2, int32(labelY), labelFontSize, rl.Black)
	rl.DrawText("VX", int32(vxX+inputW/2)-rl.MeasureText("VX", labelFontSize)/2, int32(labelY), labelFontSize, rl.Black)
	rl.DrawText("VY", int32(vyX+inputW/2)-rl.MeasureText("VY", labelFontSize)/2, int32(labelY), labelFontSize, rl.Black)
	rl.DrawText("Color", int32(colorX+colorW/2)-rl.MeasureText("Color", labelFontSize)/2, int32(labelY), labelFontSize, rl.Black)
}

func spawnBallIfRequested(spawn bool, balls *[]*MovingCircle, xStr, yStr, massStr, vxStr, vyStr string, colorIdx int32, colorList []rl.Color, formHeight float32) {
	if spawn && len(*balls) < MaxBalls {
		x, _ := strconv.ParseFloat(xStr, 32)
		y, _ := strconv.ParseFloat(yStr, 32)
		mass, _ := strconv.ParseFloat(massStr, 32)
		vx, _ := strconv.ParseFloat(vxStr, 32)
		vy, _ := strconv.ParseFloat(vyStr, 32)
		xf := clampf(float32(x), 0, Width)
		yf := clampf(float32(y), formHeight, Height)
		mf := clampf(float32(mass), 1, 1000)
		vxf := float32(vx)
		vyf := float32(vy)
		color := colorList[colorIdx%int32(len(colorList))]
		*balls = append(*balls, NewMovingCircle(xf, yf, mf, vxf, vyf, color))
	}
}

func updateBalls(balls []*MovingCircle, formHeight float32) {
	for _, c := range balls {
		c.UpdatePosition(formHeight)
	}
	for i := range balls {
		for j := i + 1; j < len(balls); j++ {
			handleCollision(balls[i], balls[j])
		}
	}
}

func drawBalls(balls []*MovingCircle) {
	for _, c := range balls {
		c.Render()
	}
}

func main() {
	rl.InitWindow(Width, Height, "Moving Balls (With Spawn Form & Mouse Focus)")
	defer rl.CloseWindow()
	rl.SetTargetFPS(60)

	// Form state
	xStr := "400"
	yStr := "300"
	massStr := "10"
	vxStr := "0"
	vyStr := "0"
	colorIdx := int32(0)
	colorNames := []string{"Gold", "Beige", "Red", "Blue", "Green", "Purple"}
	colorList := []rl.Color{rl.Gold, rl.Beige, rl.Red, rl.Blue, rl.Green, rl.Purple}

	mass := float32(20.0)

	r := radiusFromMass(mass)
	b := r * float32(math.Sqrt(2))

	balls := []*MovingCircle{
		NewMovingCircle(100, 300, mass, 2, 0, rl.Gold),             // moving ball
		NewMovingCircle(400, 300+float32(b), mass, 0, 0, rl.Beige), // stationary ball, offset by b
	}

	var focused int32

	// Layout
	formHeight := float32(50)
	labelY := float32(7)
	inputY := float32(25)
	labelFontSize := int32(16)
	inputW := float32(60)
	inputH := float32(22)
	spacing := float32(10)

	// Field positions
	xX := float32(10)
	yX := xX + inputW + spacing
	mX := yX + inputW + spacing
	vxX := mX + inputW + spacing
	vyX := vxX + inputW + spacing
	colorX := vyX + inputW + spacing
	colorW := float32(90)
	spawnX := colorX + colorW + spacing
	spawnW := float32(80)
	spawnH := inputH

	// Field rectangles for mouse focus
	rectX := rl.NewRectangle(xX, inputY, inputW, inputH)
	rectY := rl.NewRectangle(yX, inputY, inputW, inputH)
	rectM := rl.NewRectangle(mX, inputY, inputW, inputH)
	rectVX := rl.NewRectangle(vxX, inputY, inputW, inputH)
	rectVY := rl.NewRectangle(vyX, inputY, inputW, inputH)
	rectColor := rl.NewRectangle(colorX, inputY, colorW, inputH)
	rectSpawn := rl.NewRectangle(spawnX, inputY, spawnW, spawnH)

	// For dropdown expanded area
	dropdownItemCount := int32(len(colorNames))
	dropdownItemHeight := inputH
	dropdownExpandedHeight := dropdownItemHeight*float32(dropdownItemCount) + gui.BORDER_WIDTH
	rectColorExpanded := rl.NewRectangle(colorX, inputY+inputH, colorW, dropdownExpandedHeight)

	rects := []rl.Rectangle{rectX, rectY, rectM, rectVX, rectVY, rectColor}

	for !rl.WindowShouldClose() {
		mouse := rl.GetMousePosition()

		handleMouseFocus(mouse, rects, rectColorExpanded, &focused)

		// --- PHYSICS & LOGIC ---
		spawn := false // We'll set this after GUI widgets

		// --- DRAW ---
		rl.BeginDrawing()
		rl.ClearBackground(rl.NewColor(0x13, 0x13, 0x13, 0xff))

		// 1. Draw form background
		rl.DrawRectangle(0, 0, Width, int32(formHeight), rl.Fade(rl.Gray, 0.5))

		// 2. Draw balls (so they appear under the GUI)
		drawBalls(balls)

		for i, c := range balls {
			// Use a different color for each ball's velocity vector for clarity
			vecColor := rl.White
			if i == 0 {
				vecColor = rl.Yellow
			} else if i == 1 {
				vecColor = rl.SkyBlue
			}
			drawVelocityVector(c, vecColor)
		}

		// 3. Draw GUI widgets
		drawFormLabels(xX, yX, mX, vxX, vyX, colorX, inputW, colorW, labelY, labelFontSize)
		gui.Lock()
		gui.SetStyle(gui.DEFAULT, gui.TEXT_SIZE, 18)
		gui.Unlock()
		gui.TextBox(rectX, &xStr, 8, focused == 1)
		gui.TextBox(rectY, &yStr, 8, focused == 2)
		gui.TextBox(rectM, &massStr, 8, focused == 3)
		gui.TextBox(rectVX, &vxStr, 8, focused == 4)
		gui.TextBox(rectVY, &vyStr, 8, focused == 5)
		gui.DropdownBox(rectColor, colorNamesString(colorNames), &colorIdx, focused == 6)
		spawn = gui.Button(rectSpawn, "Spawn")

		// 4. Draw ball count
		rl.DrawText("Balls: "+strconv.Itoa(len(balls)), 560, 10, 18, rl.White)

		rl.EndDrawing()

		// --- LOGIC after drawing ---
		spawnBallIfRequested(spawn, &balls, xStr, yStr, massStr, vxStr, vyStr, colorIdx, colorList, formHeight)
		updateBalls(balls, formHeight)
	}

}
