import { BaseDTO } from "../BaseDTO.mjs";

export class RoundTimerUpdateRequestDTO extends BaseDTO {
    // Properties declaration. If true, the property is required
    gameRoomId = true;

    // Override the setProperties method
    setProperties(dataJson) {
        super.setProperties(this, dataJson);
    }
}
