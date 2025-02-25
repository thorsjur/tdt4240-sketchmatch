import { BaseDTO } from "../BaseDTO.mjs";

export class TimerTickResponseDTO extends BaseDTO {
    // Properties declaration. If true, the property is required
    timerTick = true;

    // Override the setProperties method
    setProperties(dataJson) {
        super.setProperties(this, dataJson);
    }
}
