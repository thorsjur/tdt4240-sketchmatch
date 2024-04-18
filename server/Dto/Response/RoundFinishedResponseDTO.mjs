import { BaseDTO } from "../BaseDTO.mjs";

export class RoundFinishedResponseDTO extends BaseDTO {
    // Properties declaration. If true, the property is required
    gameRoom = true;

    // Override the setProperties method
    setProperties(dataJson) {
        super.setProperties(this, dataJson);
    }
}