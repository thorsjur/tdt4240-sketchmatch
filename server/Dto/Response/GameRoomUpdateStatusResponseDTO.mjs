import { BaseDTO } from "../BaseDTO.mjs";

export class GameRoomUpdateStatusResponseDTO extends BaseDTO {
    // Properties declaration. If true, the property is required
    gameRoom = true;
    message = true;
    status = true;

    // Override the setProperties method
    setProperties(dataJson) {
        super.setProperties(this, dataJson);
    }
}
