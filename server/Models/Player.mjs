export class Player {
    constructor(id, hwid, name) {
        this.id = id;
        this.hwid = hwid;
        this.name = name;
        this.score = 0;
    }

    // Set nickname
    setNickname(nickname) {
        this.nickname = nickname;
    }
}