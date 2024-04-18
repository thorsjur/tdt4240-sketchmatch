import { BaseDTO } from "../BaseDTO.mjs";

export class SetDrawWordResponseDTO extends BaseDTO {
    // Properties declaration. If true, the property is required
    status = "success";
    message = "set_draw_word_success_msg";
    gameRoom;

    // Override the setProperties method
    setProperties(dataJson) {
        super.setProperties(this, dataJson);
    }
}
