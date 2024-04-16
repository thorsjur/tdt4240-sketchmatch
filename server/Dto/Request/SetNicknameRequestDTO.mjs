import { BaseDTO } from '../BaseDTO.mjs';

export class SetNicknameRequestDTO extends BaseDTO {

    // Properties declaration. If true, the property is required
    nickname = true;
    hwid = true;

    // Override the setProperties method
    setProperties(dataJson) {
        super.setProperties(this, dataJson);
    }

}