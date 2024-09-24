const VALID_XS = new Set([-2, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2]);
const VALID_RS = new Set([1, 1.5, 2, 2.5, 3]);

document.addEventListener("DOMContentLoaded", () => {
    /** @type {HTMLFormElement} */
    const form = document.getElementById("data-form");
    form.addEventListener("submit", submit);
    initCanvas();
});

/**
 * Checks that only one X checkbox is checked.
 * @returns {boolean}
 */
function checkX() {
    const checkboxes = document.getElementsByName("x");
    let checked = false;

    for (let i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked) {
            if (checked) {
                return false;
            }
            checked = true;
        }
    }

    return true;
}

/**
 * Sends the data to the server.
 * @param ev {SubmitEvent}
 */
function submit(ev) {
    ev.preventDefault();

    /** @type {HTMLDivElement} */
    const errorDiv = document.getElementById("error");

    try {
        const data = new FormData(ev.target);
        const values = Object.fromEntries(data);
        validateInput(values);
    } catch (e) {
        errorDiv.hidden = false;
        errorDiv.textContent = e.message;
        return;
    }

    errorDiv.hidden = true;
    errorDiv.textContent = "";

    this.submit(ev);
}

/**
 * Parses input values and checks for validity.
 * @param values {Record<string, string>} Raw input values
 * @throws {Error} If input values are invalid
 */
function validateInput(values) {
    if (values.x === undefined) {
        throw new Error("x is required");
    }

    if (!VALID_XS.has(Number(values.x))) {
        throw new Error(`x must be one of [${[...VALID_XS].join(", ")}]`);
    }

    if (values.y === undefined) {
        throw new Error("y is required");
    }

    if (Number(values.y) < -5 || Number(values.y) > 5) {
        throw new Error("y must be in [-5, 5]");
    }

    if (values.r === undefined) {
        throw new Error("r is required");
    }

    if (!VALID_RS.has(Number(values.r))) {
        throw new Error(`r must be one of [${[...VALID_RS].join(", ")}]`);
    }
}

function getR() {
    const rs = Array.from(document.getElementsByName("r")).filter(el => el.checked);
    if (rs.length !== 1) {
        throw new Error("Exactly one r must be checked");
    }
    return 100 * Number(rs[0].value);
}

/**
 * Rounds to nearest half
 * @param num {number}
 * @returns {number}
 */
function roundHalf(num) {
    return Math.round(num * 2) / 2;
}

function initCanvas() {
    /** @type {HTMLCanvasElement} */
    const canvas = document.getElementById("graph");
    /** @type {CanvasRenderingContext2D} */
    const ctx = canvas.getContext("2d");

    canvas.addEventListener("click", function (event) {
        const rect = canvas.getBoundingClientRect();
        const x = event.clientX - rect.left - canvas.width / 2;
        const y = canvas.height / 2 - (event.clientY - rect.top);

        const xCoord = roundHalf(Math.round(x * (getR() / (canvas.width / 4))) / 100);
        const yCoord = roundHalf(Math.round(y * (getR() / (canvas.height / 4))) / 100);

        console.log(`Clicked coordinates: (${xCoord}, ${yCoord})`);
    });

    drawShape(ctx, canvas);
}

/**
 * Draws graph on canvas
 * @param ctx {CanvasRenderingContext2D}
 * @param canvas {HTMLCanvasElement}
 */
function drawShape(ctx, canvas) {
    const R = getR();

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    ctx.translate(canvas.width / 2, canvas.height / 2);
    ctx.scale(1, -1);

    ctx.fillStyle = 'rgb(51 153 255)'
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

