import { BaseDTO } from '../BaseDTO.mjs';

export class JoinRoomByCodeResponseDTO extends BaseDTO {

    // Properties declaration. If true, the property is required
    status = "success";
    message = "You have joined the game room successfully";
    gameRoom;

    // Override the setProperties method
    setProperties(dataJson) {
        super.setProperties(this, dataJson);
    }

}