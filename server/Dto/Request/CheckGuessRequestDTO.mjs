import { BaseDTO } from "../BaseDTO.mjs";

export class CheckGuessRequestDTO extends BaseDTO {
    // Properties declaration. If true, the property is required
    inputGuess = true;
    gameRoomId = true;
    timestamp = true;

    // Override the setProperties method
    setProperties(dataJson) {
        super.setProperties(this, dataJson);
    }
}
