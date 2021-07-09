package shared.enums;

import java.io.Serializable;

public enum MessageType implements Serializable {
    // login type messages
    LOGIN_REQ,
    SIGNUP_REQ,
    REFUSED,

    // account properties messages
    PROFILE,
    BATTLE_DECK,
    BATTLE_HISTORY,

    // and in other in game types...
    GAME_MODE,
    JOINING_GAME_REQ,
    PLAYER_JOINED,
}
