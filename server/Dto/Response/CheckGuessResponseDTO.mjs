import { BaseDTO } from "../BaseDTO.mjs";

export class CheckGuessResponseDTO extends BaseDTO {
    // Properties declaration. If true, the property is required
    guess = true;

    // Override the setProperties method
    setProperties(dataJson) {
        super.setProperties(this, dataJson);
    }
}
