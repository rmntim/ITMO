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
        errorDiv.innerText = e.message;
        return;
    }

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

document.addEventListener("DOMContentLoaded", () => {
    /** @type {HTMLFormElement} */
    const form = document.getElementById("data-form");
    form.addEventListener("submit", submit);
    initCanvas();
});