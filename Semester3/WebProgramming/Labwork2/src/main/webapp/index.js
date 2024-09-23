const VALID_XS = new Set([-2, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2]);
const VALID_RS = new Set([1, 1.5, 2, 2.5, 3]);

document.addEventListener("DOMContentLoaded", () => {
    /** @type {HTMLFormElement} */
    const form = document.getElementById("data-form");
    form.addEventListener("submit", submit);
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
async function submit(ev) {
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

    await this.submit(ev);
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