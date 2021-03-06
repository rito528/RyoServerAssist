ALTER TABLE Players ADD PRIMARY KEY(UUID(128));
ALTER TABLE Players
CHANGE COLUMN UUID uuid TEXT,
CHANGE COLUMN lastLogin last_login DATETIME,
CHANGE COLUMN lastLogout last_logout DATETIME,
CHANGE COLUMN loginDays login_days INT,
CHANGE COLUMN consecutiveLoginDays consecutive_login_days INT,
CHANGE COLUMN lastDistributionReceived last_distribution_received INT,
CHANGE COLUMN EXP exp DOUBLE,
CHANGE COLUMN LEVEL level INT,
CHANGE COLUMN questClearTimes quest_clear_times INT,
CHANGE COLUMN gachaTickets gacha_tickets INT,
CHANGE COLUMN gachaPullNumber gacha_pull_number INT,
CHANGE COLUMN SkillPoint skill_point DOUBLE,
CHANGE COLUMN SkillOpenPoint skill_open_point INT,
CHANGE COLUMN OpenedSkills opened_skills TEXT,
CHANGE COLUMN SpecialSkillOpenPoint special_skill_open_point INT,
CHANGE COLUMN OpenedSpecialSkills opened_special_skills TEXT,
CHANGE COLUMN OpenedTitles opened_titles TEXT,
CHANGE COLUMN SelectedTitle selected_title TEXT,
CHANGE COLUMN EventTitles event_titles TEXT,
CHANGE COLUMN autoStack is_auto_stack BOOLEAN,
CHANGE COLUMN VoteNumber vote_number INT,
CHANGE COLUMN LastVote last_vote DATETIME,
CHANGE COLUMN ContinueVoteNumber continue_vote_number INT,
CHANGE COLUMN LastDailyQuest last_daily_quest_time DATETIME;
