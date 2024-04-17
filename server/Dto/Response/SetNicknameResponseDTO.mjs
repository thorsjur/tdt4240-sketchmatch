import { BaseDTO } from '../BaseDTO.mjs';

export class SetNicknameResponsetDTO extends BaseDTO {

    // Properties declaration. If true, the property is required
    status = "success";
    message = "set_nickname_success_msg";
    player;

    // Override the setProperties method
    setProperties(dataJson) {
        super.setProperties(this, dataJson);
    }

}