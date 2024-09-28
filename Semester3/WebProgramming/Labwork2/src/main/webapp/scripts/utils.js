const VALID_XS = new Set([-2, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2]);
const VALID_RS = new Set([1, 1.5, 2, 2.5, 3]);

/**
 * Gets R value
 * @returns {number} R multiplied by 100
 * @throws {Error} If no R is checked or multiple Rs are checked (should be impossible but why not)
 */
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

/**
 * Checks that only one X checkbox is checked.
 * @returns {boolean}
 */
function checkX() {
    /** @type {NodeListOf<HTMLInputElement>} */
    const checkboxes = document.querySelectorAll("input[type='checkbox'][name^='x-']");
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
