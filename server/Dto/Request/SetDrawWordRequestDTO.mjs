import { BaseDTO } from "../BaseDTO.mjs";

export class SetDrawWordRequestDTO extends BaseDTO {
    // Properties declaration. If true, the property is required
    drawWord = true;
    difficulty = true;
    gameRoomId = true;

    // Override the setProperties method
    setProperties(dataJson) {
        super.setProperties(this, dataJson);
    }
}
