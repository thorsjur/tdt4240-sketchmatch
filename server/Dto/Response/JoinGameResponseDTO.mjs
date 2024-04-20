import { BaseDTO } from '../BaseDTO.mjs';

export class JoinGameResponseDTO extends BaseDTO {

    // Properties declaration. If true, the property is required
    status = "success";
    message = "game_room_enter_success";
    gameRoom;

    // Override the setProperties method
    setProperties(dataJson) {
        super.setProperties(this, dataJson);
    }

}