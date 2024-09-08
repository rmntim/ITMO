"use strict";

class InvalidValueException extends Error {
    constructor(message) {
        super(message);
        this.name = "InvalidValueException";
    }
}

/**
 * @typedef {Object} FormValues
 * @property {string|undefined} x
 * @property {string} y
 * @property {string} r
 */

/** @param values FormValues
 * @throws InvalidValueException If any of the values are not valid
 */
function validateFormInput(values) {
    if (values.x === undefined) {
        throw new InvalidValueException("please select x");
    }

    if (isNaN(values.y)) {
        throw new InvalidValueException("invalid y value");
    }

    const y = parseInt(values.y);
    if (y < -3 || y > 5) {
        throw new InvalidValueException("y is out of bounds (allowed range -3..5)")
    }

    if (isNaN(values.r)) {
        throw new InvalidValueException("invalid r value")
    }
}

/** @type HTMLTableElement */
const table = document.getElementById("result-table");

/** @type HTMLDivElement */
const errorDiv = document.getElementById("error");

/** @param {SubmitEvent} ev */
async function onSubmit(ev) {
    ev.preventDefault();

    const formData = new FormData(this);
    /** @type FormValues */
    const values = Object.fromEntries(formData);

    try {
        validateFormInput(values);
        errorDiv.hidden = true;
    } catch (e) {
        this.reset();
        errorDiv.hidden = false;
        errorDiv.textContent = e.message;
        return;
    }

    const params = new URLSearchParams(formData);
    const url = "http://localhost:8080/calculate?" + params.toString();

    const response = await fetch(url);

    const newRow = table.insertRow(-1);

    const rowX = newRow.insertCell(0);
    const rowY = newRow.insertCell(1);
    const rowR = newRow.insertCell(2);
    const rowResult = newRow.insertCell(3);

    rowX.textContent = values.x;
    rowY.textContent = values.y;
    rowR.textContent = values.r;

    if (response.ok) {
        /** @type {{result: boolean}} */
        const result = await response.json();
        rowResult.textContent = result.result.toString();
    } else {
        /** @type {{reason: string}} */
        const result = await response.json();
        rowResult.textContent = "error";
        console.error(result);
    }
}


/** @type HTMLFormElement */
const form = document.getElementById("data-form");
form.addEventListener("submit", onSubmit);