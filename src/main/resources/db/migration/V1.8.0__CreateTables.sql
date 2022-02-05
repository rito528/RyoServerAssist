USE RyoServerAssist;

CREATE TABLE IF NOT EXISTS firstJoinItems(
    id INT AUTO_INCREMENT,
    ItemStack TEXT,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS Quests(
    id INT AUTO_INCREMENT,
    UUID TEXT,
    selectedQuest TEXT,
    remaining TEXT,
    bookmarks TEXT,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS StackData(
    id INT AUTO_INCREMENT,
    UUID TEXT,
    category TEXT,
    item TEXT,
    amount INT,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS StackList(
    id INT AUTO_INCREMENT,
    category TEXT,
    PAGE INT,
    invItem TEXT,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS GachaItems(
    id INT AUTO_INCREMENT,
    Rarity INT,
    Material TEXT,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS Distribution(
    id INT AUTO_INCREMENT,
    GachaPaperType TEXT,
    COUNT INT,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS Homes(
    UUID TEXT,
    POINT INT,
    Location TEXT,
    Locked BOOLEAN
);

CREATE TABLE IF NOT EXISTS Players(
    UUID TEXT,
    lastLogin DATETIME,
    lastLogout DATETIME,
    loginDays INT,
    consecutiveLoginDays INT,
    lastDistributionReceived INT,
    EXP DOUBLE,
    LEVEL INT,
    questClearTimes INT,
    gachaTickets INT,
    gachaPullNumber INT,
    SkillPoint DOUBLE,
    SkillOpenPoint INT,
    OpenedSkills TEXT,
    SpecialSkillOpenPoint INT DEFAULT 0,
    OpenedSpecialSkills TEXT,
    OpenedTitles TEXT,
    SelectedTitle TEXT,
    EventTitles TEXT,
    autoStack BOOLEAN,
    VoteNumber INT,
    LastVote DATETIME DEFAULT "2022-01-01 00:00:00",
    ContinueVoteNumber INT DEFAULT 0,
    LastDailyQuest DATETIME DEFAULT "2022-01-01 00:00:00",
    Twitter TEXT,
    Discord TEXT,
    Word TEXT
);

CREATE TABLE IF NOT EXISTS Events(
    EventName TEXT NOT NULL,
    counter INT,
    GivenGachaTickets INT DEFAULT 0,
    PRIMARY KEY(EventName(64))
);

CREATE TABLE IF NOT EXISTS EventRankings(
    UUID TEXT,
    EventName TEXT,
    counter INT
);

CREATE TABLE IF NOT EXISTS Storage(UUID TEXT, invData TEXT);
CREATE TABLE IF NOT EXISTS AdminStorage(invData TEXT);