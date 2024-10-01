/**
 * Initializes graph canvas
 */
function initCanvas() {
    /** @type {HTMLCanvasElement} */
    const canvas = document.getElementById("graph");
    /** @type {CanvasRenderingContext2D} */
    const ctx = canvas.getContext("2d");

    canvas.addEventListener("click", function (event) {
        const rect = canvas.getBoundingClientRect();
        const xDom = event.clientX - rect.left - canvas.width / 2;
        const yDom = canvas.height / 2 - (event.clientY - rect.top);

        try {
            const r = getR();
            const x = roundHalf(
                Math.round(xDom * (r / (canvas.width / 4))) / 100
            );
            const y = roundHalf(
                Math.round(yDom * (r / (canvas.height / 4))) / 100
            );
            sendPoint(x, y, r);
        } catch (e) {
            /** @type {HTMLDivElement} */
            const errorDiv = document.getElementById("error");
            errorDiv.hidden = false;
            errorDiv.innerText = e.message;
        }
    });

    drawShape(ctx, canvas);
}

/**
 * Sends clicked point to server
 * @param x {number}
 * @param y {number}
 * @param r {number}
 */
function sendPoint(x, y, r) {
    /** @type {HTMLFormElement} */
    const form = document.getElementById("data-form");

    let didCheckX = false;
    for (/** @type {HTMLInputElement} */ const checkbox of form["x"]) {
        if (checkbox.value === x.toString()) {
            checkbox.checked = true;
            didCheckX = true;
        } else {
            checkbox.checked = false;
        }
    }

    if (!didCheckX) {
        throw new Error("x is invalid");
    }

    form["y"].value = y;
    form["r"].value = r;

    form.submit();
}

/**
 * Draws graph on canvas
 * @param ctx {CanvasRenderingContext2D}
 * @param canvas {HTMLCanvasElement}
 */
function drawShape(ctx, canvas) {
    const R = 100;

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    ctx.translate(canvas.width / 2, canvas.height / 2);
    ctx.scale(1, -1);

    ctx.fillStyle = "rgb(51 153 255)";
    ctx.beginPath();

    // Top left triangle
    ctx.moveTo(0, 0);
    ctx.lineTo(0, R);
    ctx.lineTo(-R / 2, 0);

    // Bottom right rectangle
    ctx.moveTo(0, 0);
    ctx.lineTo(0, -R / 2);
    ctx.lineTo(-R, -R / 2);
    ctx.lineTo(-R, 0);

    // Bottom left circle
    ctx.arc(0, 0, R / 2, 0, -Math.PI / 2, true);

    ctx.closePath();
    ctx.fill();

    // Axis
    ctx.strokeStyle = "white";
    ctx.beginPath();
    ctx.moveTo(-canvas.width / 2, 0);
    ctx.lineTo(canvas.width / 2, 0);
    ctx.moveTo(0, -canvas.height / 2);
    ctx.lineTo(0, canvas.height / 2);
    ctx.stroke();

    ctx.setTransform(1, 0, 0, 1, 0, 0);

    ctx.fillStyle = "white";
    ctx.font = "12px monospace";
    ctx.fillText("R", canvas.width / 2 + 6, canvas.height / 2 - R);
    ctx.fillText("R/2", canvas.width / 2 + 6, canvas.height / 2 - R / 2);
    ctx.fillText("R", canvas.width / 2 + R - 6, canvas.height / 2 + 12);
    ctx.fillText("R/2", canvas.width / 2 + R / 2 - 6, canvas.height / 2 + 12);
    ctx.fillText("-R/2", canvas.width / 2 - R / 2 - 12, canvas.height / 2 + 12);
    ctx.fillText("-R", canvas.width / 2 - R - 12, canvas.height / 2 + 15);
    ctx.fillText("-R/2", canvas.width / 2 + 6, canvas.height / 2 + R / 2 + 6);
    ctx.fillText("-R", canvas.width / 2 + 6, canvas.height / 2 + R + 6);
}
