package script.library;

import script.*;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

public class skill extends script.base_script
{
    public skill()
    {
    }
    public static final String DELIM_RANGE = "..";
    public static final String SCRIPTVAR_SKILLS = "trainer.skills";
    public static final String SCRIPTVAR_JEDI_SKILLS = "trainer.jedi_skills";
    public static final String DATATABLE_SKILL_TEMPLATE = "datatables/skill_template/skill_template.iff";
    public static final String DATATABLE_RACIAL_STATS = "datatables/skill/racial_stats.iff";
    public static final String DATATABLE_EXPERTISE = "datatables/expertise/expertise.iff";
    public static final String DATATABLE_TEMPLATE = "TEMPLATE";
    public static final String DATATABLE_BASE_SKILLS = "BASE_SKILLS";
    public static final String DATATABLE_INHERITS_LIST = "INHERITS_TEMPLATES";
    public static final String DATATABLE_RECURSE = "RECURSE";
    public static final String PLAYER_BASE = "player_base";
    public static final String PLAYER_BETA = "player_beta";
    public static final String DICT_CODE = "code";
    public static final String DICT_DELTA = "delta";
    public static final String DICT_DELTA_SCALE = "deltaScale";
    public static final String DICT_DELTA_PERCENT = "deltaPercent";
    public static final int CODE_FAIL = 0;
    public static final int CODE_PASS = 1;
    public static final int CODE_ERROR = -1;
    public static final int CODE_INSUFFICIENT_SKILL = -2;
    public static final int PHASE_ONE = 1;
    public static final int PHASE_TWO = 2;
    public static final int PHASE_THREE = 3;
    public static final int PHASE_FOUR = 4;
    public static final int HEALTH_POINTS_PER_STAMINA = 2;
    public static final int HEALTH_POINTS_PER_CONSTITUTION = 8;
    public static final int ACTION_POINTS_PER_STAMINA = 8;
    public static final int ACTION_POINTS_PER_CONSTITUTION = 2;
    public static final int NUM_STATS = 6;
    public static final String[] WEAPON_TYPES = 
    {
        "unarmed",
        "polearm",
        "sword1h",
        "sword2h",
        "rifle",
        "carbine",
        "pistol"
    };
    public static final String[] MOD_TYPES = 
    {
        "accuracy",
        "speed",
        "damage"
    };
    public static final String DICT_SKILLNAME = "skillName";
    public static final String HANDLER_SKILL_GRANTED = "skillGranted";
    public static final String JEDI_SKILL_REQUIREMENTS_DATATABLE = "datatables/jedi/jedi_skill_requirements.iff";
    public static final String DEFAULT_SKILL_GRANT_SOUND = "sound/music_acq_bountyhunter.snd";
    public static final string_id PROSE_NSF_SKILL_PTS = new string_id("base_player", "prose_nsf_skill_pts");
    public static final string_id PROSE_BAD_SPECIES = new string_id("base_player", "prose_bad_species");
    public static final String TBL_SKILL = "datatables/skill/skills.iff";
    public static final int SKILL_POINT_CAP = 250;
    public static final int PRECU_COMBAT_SKILL_SCORE_MAX = 90;
    public static final int PRECU_ADVANCED_COMBAT_SKILL_WEIGHT = 3;
    public static final int PRECU_PHASE_TWO_COMBAT_SCORE = 25;
    public static final int PRECU_PHASE_THREE_COMBAT_SCORE = 50;
    public static final int PRECU_PHASE_FOUR_COMBAT_SCORE = 75;
    public static final String SKILL_N = "skl_n";
    public static final String CONVOFILE = "skill_teacher";
    public static final string_id PROSE_SKILL_LEARNED = new string_id(CONVOFILE, "prose_skill_learned");
    public static final string_id PROSE_TRAIN_FAILED = new string_id(CONVOFILE, "prose_train_failed");
    public static final string_id SID_EXPERTISE_WRONG_PROFESSION = new string_id("spam", "expertise_wrong_profession");
    public static boolean isRetiredNgeProgressionSkillName(String skillName) throws InterruptedException
    {
        return skillName != null &&
            (skillName.startsWith("class_") ||
                skillName.equals("expertise") ||
                skillName.startsWith("expertise_") ||
                skillName.startsWith("internal_expertise_"));
    }
    public static boolean isRetiredPostNgeSpySkill(String skillName) throws InterruptedException
    {
        return skillName != null &&
            (skillName.startsWith("class_spy_") ||
                skillName.startsWith("expertise_sp_"));
    }
    public static boolean grant(obj_id target, String skillName) throws InterruptedException
    {
        if (isPlayer(target) && isRetiredPostNgeSpySkill(skillName))
        {
            return false;
        }
        if (grantSkillToPlayer(target, skillName))
        {
            dictionary d = new dictionary();
            d.put(DICT_SKILLNAME, skillName);
            messageTo(target, HANDLER_SKILL_GRANTED, d, 0, true);
            return true;
        }
        return grantSkill(target, skillName);
    }
    public static boolean grantSkillToPlayer(obj_id player, String skillName) throws InterruptedException
    {
        if (!isIdValid(player) || (!isPlayer(player)) || skillName == null || skillName.equals(""))
        {
            return false;
        }
        if (isRetiredPostNgeSpySkill(skillName))
        {
            return false;
        }
        string_id sid_skillName = new string_id("skl_n", skillName);
        String dtSpeciesReq = dataTableGetString("datatables/skill/skills.iff", skillName, "SPECIES_REQUIRED");
        if (dtSpeciesReq != null && !dtSpeciesReq.equals(""))
        {
            dictionary species = getSkillPrerequisiteSpecies(skillName);
            int speciesId = getSpecies(player);
            String speciesName = utils.getPlayerSpeciesName(speciesId);
            boolean allowSpecies = species.getBoolean(speciesName);
            if (!allowSpecies)
            {
                string_id sid_species = new string_id("species", speciesName);
                prose_package ppBadSpecies = prose.getPackage(PROSE_BAD_SPECIES, sid_species, sid_skillName);
                sendSystemMessageProse(player, ppBadSpecies);
                return false;
            }
        }
        if (grantSkill(player, skillName))
        {
            String soundFile = DEFAULT_SKILL_GRANT_SOUND;
            if (skillName.startsWith("combat_"))
            {
                soundFile = DEFAULT_SKILL_GRANT_SOUND;
            }
            else if (utils.isProfession(player, utils.TRADER))
            {
                soundFile = "sound/music_acq_academic.snd";
            }
            else if (skillName.startsWith("outdoors_"))
            {
                if (skillName.startsWith("outdoors_miner_") || skillName.startsWith("outdoors_farmer_"))
                {
                    soundFile = "sound/music_acq_academic.snd";
                }
                else 
                {
                    soundFile = DEFAULT_SKILL_GRANT_SOUND;
                }
            }
            else if (skillName.startsWith("science_"))
            {
                soundFile = "sound/music_acq_healer.snd";
            }
            else if (skillName.startsWith("social_"))
            {
                soundFile = "sound/music_acq_thespian.snd";
            }
            else if (skillName.startsWith("pilot_neutral"))
            {
                if (space_flags.isSpaceTrack(player, space_flags.PRIVATEER_TATOOINE))
                {
                    soundFile = "sound/music_themequest_acc_criminal.snd";
                }
                else 
                {
                    soundFile = "sound/music_themequest_acc_general.snd";
                }
            }
            else if (skillName.startsWith("pilot_rebel"))
            {
                soundFile = "sound/music_themequest_acc_rebel.snd";
            }
            else if (skillName.startsWith("pilot_imperial"))
            {
                soundFile = "sound/music_themequest_acc_imperial.snd";
            }
            if (soundFile.equals(""))
            {
                playMusic(player, DEFAULT_SKILL_GRANT_SOUND);
            }
            else 
            {
                playMusic(player, soundFile);
            }
            return true;
        }
        return false;
    }
    public static int getSkillPointCost(String skillName) throws InterruptedException
    {
        if (skillName == null || skillName.equals(""))
        {
            return -1;
        }
        int row = dataTableSearchColumnForString(skillName, "NAME", TBL_SKILL);
        if (row < 0)
        {
            return -1;
        }
        int pointsRequired = dataTableGetInt(TBL_SKILL, row, "POINTS_REQUIRED");
        return pointsRequired < 0 ? -1 : pointsRequired;
    }
    public static int getAvailableSkillPoints(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !isPlayer(player))
        {
            return 0;
        }
        String[] playerSkills = getSkillListingForPlayer(player);
        if (playerSkills == null || playerSkills.length == 0)
        {
            return SKILL_POINT_CAP;
        }
        int usedPoints = 0;
        for (String playerSkill : playerSkills)
        {
            int skillCost = getSkillPointCost(playerSkill);
            if (skillCost < 0)
            {
                return 0;
            }
            usedPoints += skillCost;
            if (usedPoints > SKILL_POINT_CAP)
            {
                return 0;
            }
        }
        return SKILL_POINT_CAP - usedPoints;
    }
    public static boolean purchaseSkill(obj_id player, String skillName) throws InterruptedException
    {
        if (!isIdValid(player) || (!isPlayer(player)) || skillName == null || skillName.equals(""))
        {
            return false;
        }
        if (isRetiredPostNgeSpySkill(skillName))
        {
            return false;
        }
        int pointsRequired = getSkillPointCost(skillName);
        if (pointsRequired < 0 || getAvailableSkillPoints(player) < pointsRequired)
        {
            return false;
        }
        boolean hasSkills = hasRequiredSkillsForSkillPurchase(player, skillName);
        boolean hasXp = hasRequiredXpForSkillPurchase(player, skillName);
        boolean alreadyHasSkill = hasSkill(player, skillName);
        if (hasSkills && hasXp && !alreadyHasSkill)
        {
            boolean skillGrantSuccessful = false;
            if (skillName.startsWith("pilot_"))
            {
                skillGrantSuccessful = noisyGrantSkill(player, skillName);
            }
            else 
            {
                skillGrantSuccessful = grantSkillToPlayer(player, skillName);
            }
            if (skillGrantSuccessful)
            {
                if (deductXpCostForSkillPurchase(player, skillName))
                {
                    dictionary holocronParams = new dictionary();
                    holocronParams.put("eventName", "TrainedSkillBox");
                    messageTo(player, "handleHolocronEvent", holocronParams, 0, false);
                    return true;
                }
                else 
                {
                    revokeSkill(player, skillName);
                    CustomerServiceLog("Skill", "skill.purchaseSkill(): (" + player + ") " + getName(player) + " was unable to pay xp costs and had skill '" + skillName + "' revoked during purchase");
                    return false;
                }
            }
            return false;
        }
        return false;
    }
    public static boolean hasRequiredSkillsForSkillPurchase(obj_id player, String skillName) throws InterruptedException {
        if (!isIdValid(player) || (!isPlayer(player)) || skillName == null || skillName.equals("")) {
            return false;
        }
        String[] pSkills = getSkillListingForPlayer(player);
        String[] skillReqs = getSkillPrerequisiteSkills(skillName);
        return skillReqs == null || pSkills != null && utils.isSubset(pSkills, skillReqs);
    }
    public static boolean hasRequiredXpForSkillPurchase(obj_id player, String skillName) throws InterruptedException
    {
        if (!isIdValid(player) || (!isPlayer(player)) || skillName == null || skillName.equals(""))
        {
            return false;
        }
        dictionary xpReqs = getSkillPrerequisiteExperience(skillName);
        if ((xpReqs == null) || (xpReqs.isEmpty()))
        {
            return true;
        }
        boolean qualifies = true;
        java.util.Enumeration e = xpReqs.keys();
        String xpType;
        while (e.hasMoreElements())
        {
            xpType = (String) (e.nextElement());
            int xpCost = xpReqs.getInt(xpType);
            if (getExperiencePoints(player, xpType) < xpCost)
            {
                qualifies = false;
            }
        }
        return qualifies;
    }
    public static boolean deductXpCostForSkillPurchase(obj_id player, String skillName) throws InterruptedException
    {
        if (!isIdValid(player) || (!isPlayer(player)) || skillName == null || skillName.equals(""))
        {
            return false;
        }
        dictionary xpReqs = getSkillPrerequisiteExperience(skillName);
        if ((xpReqs == null) || (xpReqs.isEmpty()))
        {
            return true;
        }
        boolean qualifies = true;
        java.util.Enumeration e = xpReqs.keys();
        String xpType;
        while (e.hasMoreElements())
        {
            xpType = (String) (e.nextElement());
            int xpCost = xpReqs.getInt(xpType);
            if (xpCost != 0)
            {
                if (getExperiencePoints(player, xpType) < xpCost)
                {
                    qualifies = false;
                }
                else 
                {
                    qualifies &= (grantExperiencePoints(player, xpType, -xpCost) != XP_ERROR);
                }
            }
        }
        return qualifies;
    }
    public static String[] getAllRequiredSkills(String skillName) throws InterruptedException
    {
        if (skillName == null || skillName.equals(""))
        {
            return null;
        }
        String[] reqs = getSkillPrerequisiteSkills(skillName);
        if ((reqs == null) || (reqs.length == 0))
        {
            return null;
        }
        Vector ret = new Vector(Arrays.asList(reqs));
        String[] tmp;
        for (String req : reqs) {
            tmp = getAllRequiredSkills(req);
            if ((tmp != null) && (tmp.length != 0)) {
                for (String aTmp : tmp) {
                    int pos = utils.getElementPositionInArray(ret, aTmp);
                    if (pos == -1) {
                        ret = utils.addElement(ret, aTmp);
                    }
                }
            }
        }
        String[] _ret = new String[0];
        if (ret != null)
        {
            _ret = new String[ret.size()];
            ret.toArray(_ret);
        }
        return _ret;
    }
    public static boolean assignSkillTemplate(obj_id target, String template) throws InterruptedException
    {
        if (!isIdValid(target) || (!isMob(target)) || template == null || template.equals(""))
        {
            return false;
        }
        if (dataTableOpen(DATATABLE_SKILL_TEMPLATE))
        {
            String[] templates = dataTableGetStringColumn(DATATABLE_SKILL_TEMPLATE, DATATABLE_TEMPLATE);
            if ((templates == null) || (templates.length == 0))
            {
                return false;
            }
            String skill_list;
            for (int i = 0; i < templates.length; i++)
            {
                if ((toLower(templates[i])).equals(toLower(template)))
                {
                    skill_list = dataTableGetString(DATATABLE_SKILL_TEMPLATE, i, DATATABLE_BASE_SKILLS);
                    if (skill_list.startsWith("\""))
                    {
                        skill_list = skill_list.substring(1);
                    }
                    if (skill_list.endsWith("\""))
                    {
                        skill_list = skill_list.substring(0, skill_list.length() - 1);
                    }
                    if (!skill_list.equals(""))
                    {
                        boolean recurse = dataTableGetInt(DATATABLE_SKILL_TEMPLATE, i, DATATABLE_RECURSE) == 1;
                        boolean litmus = true;
                        java.util.StringTokenizer skills = new java.util.StringTokenizer(toLower(skill_list), ",");
                        String skillName;
                        String[] reqs;
                        while (skills.hasMoreTokens())
                        {
                            skillName = skills.nextToken();
                            litmus &= grantSkill(target, skillName);
                            if (recurse)
                            {
                                reqs = getAllRequiredSkills(skillName);
                                if ((reqs != null) && (reqs.length != 0)) {
                                    for (String req : reqs) {
                                        litmus &= grantSkill(target, req);
                                    }
                                }
                            }
                        }
                        String inherits_list = dataTableGetString(DATATABLE_SKILL_TEMPLATE, i, DATATABLE_INHERITS_LIST);
                        if ((inherits_list != null) && (!inherits_list.equals("")))
                        {
                            java.util.StringTokenizer inheritTokens = new java.util.StringTokenizer(toLower(inherits_list), ",");
                            String inheritTemplate;
                            while (inheritTokens.hasMoreTokens())
                            {
                                inheritTemplate = inheritTokens.nextToken();
                                if (!inheritTemplate.equals(""))
                                {
                                    litmus &= assignSkillTemplate(target, inheritTemplate);
                                }
                            }
                        }
                        return litmus;
                    }
                }
            }
        }
        return false;
    }
    public static boolean revokeAllSkills(obj_id target) throws InterruptedException
    {
        if (!isIdValid(target) || (!isMob(target)))
        {
            return false;
        }
        String skills[] = getSkillListingForPlayer(target);
        if ((skills == null) || (skills.length == 0))
        {
            return false;
        }
        for (String skill : skills) {
            revokeSkill(target, skill);
            CustomerServiceLog("Skill", "skill.revokeAllSkills(): (" + target + ") " + getName(target) + " is having all skills revoked!");
        }
        return true;
    }
    public static void revokeAllProfessionSkills(obj_id player) throws InterruptedException
    {
        String[] skillList = getSkillListingForPlayer(player);
        int attempts = skillList.length;
        if ((skillList.length != 0))
        {
            while (skillList.length > 0 && attempts > 0)
            {
                for (String skillName : skillList) {
                    if (!skillName.startsWith("species_") && !skillName.startsWith("social_language_") && !skillName.startsWith("social_politician_") && !skillName.startsWith("utility_") && !skillName.startsWith("common_") && !skillName.startsWith("demo_") && !skillName.startsWith("force_title_") && !skillName.startsWith("force_sensitive_") && !skillName.startsWith("combat_melee_basic") && !skillName.startsWith("pilot_") && !skillName.startsWith("internal_expertise_") && !skillName.startsWith("combat_ranged_weapon_basic") && !skillName.equals("expertise")) {
                        revokeSkillSilent(player, skillName);
                    }
                }
                skillList = getSkillListingForPlayer(player);
                --attempts;
            }
        }
    }
    public static String[] getTeachableSkills(obj_id target, obj_id teacher) throws InterruptedException
    {
        if (!isIdValid(target) || !isMob(target) || !isIdValid(teacher) || !isMob(teacher))
        {
            return null;
        }
        if (hasObjVar(target, "newbie.hasSkill") && !hasObjVar(target, "newbie.trained"))
        {
            String[] newbieSkill = new String[1];
            newbieSkill[0] = getStringObjVar(target, "newbie.hasSkill");
            return newbieSkill;
        }
        String[] targetSkills = getSkillListingForPlayer(target);
        String[] delta = deltaTeacherSkills(target, teacher);
        if (targetSkills == null || delta == null || delta.length == 0)
        {
            return null;
        }
        Vector teachableSkills = new Vector();
        teachableSkills.setSize(0);
        for (String candidate : delta)
        {
            String[] prerequisites = getSkillPrerequisiteSkills(candidate);
            if (prerequisites != null && prerequisites.length > 0 && !utils.isSubset(targetSkills, prerequisites))
            {
                continue;
            }
            if (candidate.startsWith("species_") ||
                candidate.equals("social_language_lekku") ||
                candidate.startsWith("social_language_hutt") ||
                candidate.equals("demo_combat") ||
                candidate.startsWith("force_rank") ||
                candidate.startsWith("force_title") ||
                candidate.startsWith("expertise") ||
                candidate.startsWith("pilot_"))
            {
                continue;
            }
            if (candidate.equals("jedi_light_side_journeyman_novice") && hasSkill(target, "jedi_dark_side_journeyman_novice"))
            {
                continue;
            }
            if (candidate.equals("jedi_dark_side_journeyman_novice") && hasSkill(target, "jedi_light_side_journeyman_novice"))
            {
                continue;
            }
            teachableSkills = utils.addElement(teachableSkills, candidate);
        }
        if (teachableSkills == null || teachableSkills.size() == 0)
        {
            return null;
        }
        String[] result = new String[teachableSkills.size()];
        teachableSkills.toArray(result);
        return result;
    }
    public static String[] getQualifiedTeachableSkills(obj_id target, obj_id teacher) throws InterruptedException
    {
        if (!isIdValid(target) || (!isMob(target)) || (isIdNull(teacher)) || (!isMob(teacher)))
        {
            return null;
        }
        String[] teachableSkills = getTeachableSkills(target, teacher);
        if (teachableSkills == null)
        {
            return null;
        }
        Vector qualifiedSkills = new Vector();
        qualifiedSkills.setSize(0);
        dictionary d;
        Object o;
        String xpType;
        String trainer_type;
        String branch;
        String skillName;

        for (String teachableSkill : teachableSkills) {
            boolean qualifies = true;
            d = getSkillPrerequisiteExperience(teachableSkill);
            if (d != null && !d.isEmpty()) {
                Enumeration keys = d.keys();
                while (keys.hasMoreElements()) {
                    o = keys.nextElement();
                    if (o instanceof String) {
                        xpType = (String) o;
                        int xpCost = d.getInt(xpType);
                        int playerXP = getExperiencePoints(target, xpType);
                        if (playerXP < xpCost) {
                            qualifies = false;
                        }
                    } else {
                        return null;
                    }
                }
            }
            dictionary species = getSkillPrerequisiteSpecies(teachableSkill);
            if (species != null && !species.isEmpty()) {
                String speciesName = utils.getPlayerSpeciesName(getSpecies(target));
                if (!species.getBoolean(speciesName)) {
                    qualifies = false;
                }
            }
            trainer_type = getStringObjVar(teacher, "trainer");
            if (trainer_type != null && trainer_type.equals("trainer_fs")) {
                if (qualifies) {
                    if (fs_quests.isVillageEligible(target)) {
                        branch = fs_quests.getBranchFromSkill(teachableSkill);
                        if (!fs_quests.hasUnlockedBranch(target, branch)) {
                            qualifies = false;
                        }
                    } else {
                        qualifies = false;
                    }
                }
            }
            if (hasObjVar(target, "newbie.hasSkill") && !hasObjVar(target, "newbie.trained")) {
                skillName = getStringObjVar(target, "newbie.hasSkill");
                if (skillName.equals(teachableSkill)) {
                    qualifies = true;
                }
            }
            if (qualifies) {
                qualifiedSkills = utils.addElement(qualifiedSkills, teachableSkill);
            }
        }
        if ((qualifiedSkills != null) && (qualifiedSkills.size() > 0))
        {
            String[] _qualifiedSkills = new String[qualifiedSkills.size()];
            qualifiedSkills.toArray(_qualifiedSkills);
            return _qualifiedSkills;
        }
        return null;
    }
    public static String[] deltaTeacherSkills(obj_id target, obj_id teacher) throws InterruptedException
    {
        if (!isIdValid(target) || !isIdValid(teacher))
        {
            return null;
        }
        if (isPlayer(teacher))
        {
            return deltaPlayerTeacherSkills(target, teacher);
        }
        String[] targetSkills = getSkillListingForPlayer(target);
        String[] teacherSkills = getTeacherSkills(teacher, target);
        if (targetSkills == null || teacherSkills == null)
        {
            return null;
        }
        Vector delta = new Vector();
        delta.setSize(0);
        for (String teacherSkill : teacherSkills) {
            if (utils.getElementPositionInArray(targetSkills, teacherSkill) == -1) {
                delta = utils.addElement(delta, teacherSkill);
            }
        }
        if ((delta != null) && (delta.size() != 0))
        {
            String[] _delta = new String[delta.size()];
            delta.toArray(_delta);
            return _delta;
        }
        return null;
    }
    public static String[] getTeacherSkills(obj_id teacher, obj_id target) throws InterruptedException
    {
        if (!isIdValid(teacher) || !isIdValid(target))
        {
            return null;
        }
        if (!jedi.isJediTrainerForPlayer(target, teacher))
        {
            return utils.getStringBatchScriptVar(teacher, SCRIPTVAR_SKILLS);
        }
        else 
        {
            return utils.getStringBatchScriptVar(teacher, SCRIPTVAR_JEDI_SKILLS);
        }
    }
    public static String[] deltaPlayerTeacherSkills(obj_id target, obj_id teacher) throws InterruptedException
    {
        if (!isIdValid(target) || !isIdValid(teacher))
        {
            return null;
        }
        if (!isPlayer(teacher))
        {
            return deltaTeacherSkills(target, teacher);
        }
        String[] targetSkills = getSkillListingForPlayer(target);
        String[] teacherSkills = getSkillListingForPlayer(teacher);
        if ((targetSkills == null) || (teacherSkills == null))
        {
            return null;
        }
        Vector delta = new Vector();
        delta.setSize(0);
        for (String teacherSkill : teacherSkills) {
            if (utils.getElementPositionInArray(targetSkills, teacherSkill) == -1) {
                delta = utils.addElement(delta, teacherSkill);
            }
        }
        if ((delta != null) && (delta.size() != 0))
        {
            String[] _delta = new String[delta.size()];
            delta.toArray(_delta);
            return _delta;
        }
        return null;
    }
    public static int check(obj_id target, String skillmod, String scale) throws InterruptedException
    {
        if (!isIdValid(target) || (skillmod.equals("")))
        {
            return -1;
        }
        dictionary params = skillModCheck(target, skillmod, scale);
        int retCode = params.getInt(DICT_CODE);
        if (retCode < 0)
        {
            return -1;
        }

        return params.getInt(DICT_DELTA_PERCENT);
    }
    public static dictionary skillModCheck(obj_id target, String skillmod, String scale) throws InterruptedException
    {
        dictionary ret = new dictionary();
        if (!isIdValid(target) || (skillmod.equals("")))
        {
            ret.put(DICT_CODE, CODE_ERROR);
            return ret;
        }
        int modmin = 1;
        int modmax = 100;
        int scaleIdx = scale.indexOf(DELIM_RANGE);
        if (!scale.equals("")) {
            if (scaleIdx > 0)
			{
				java.util.StringTokenizer rng = new java.util.StringTokenizer(scale, DELIM_RANGE);
				String smin;
				String smax;
				if (rng.countTokens() == 2)
				{
					smin = rng.nextToken();
					smax = rng.nextToken();
				}
				else
				{
					ret.put(DICT_CODE, CODE_ERROR);
					return ret;
				}
				modmin = utils.stringToInt(smin);
				modmax = utils.stringToInt(smax);
			}
			else
			{
				modmax = utils.stringToInt(scale);
			}
        }
        if ((modmin < 0) || (modmax <= 0) || (modmin >= modmax))
        {
            ret.put(DICT_CODE, CODE_ERROR);
            return ret;
        }
        int modval = getSkillStatMod(target, skillmod);
        if (modval < modmin)
        {
            ret.put(DICT_CODE, CODE_INSUFFICIENT_SKILL);
            return ret;
        }
        int deltaScale = modmax - modmin;
        int roll = rand(modmin, modmax);
        int delta = modval - roll;
        int deltaPercent = (int)((delta / deltaScale) * 100);
        if (delta >= 0)
        {
            ret.put(DICT_CODE, CODE_PASS);
        }
        else 
        {
            ret.put(DICT_CODE, CODE_FAIL);
        }
        ret.put(DICT_DELTA, delta);
        ret.put(DICT_DELTA_SCALE, deltaScale);
        ret.put(DICT_DELTA_PERCENT, deltaPercent);
        return ret;
    }
    public static boolean isPrecuCombatSkillBox(String skillName) throws InterruptedException
    {
        if (skillName == null || skillName.length() == 0 || skillName.indexOf("_prereq") >= 0)
        {
            return false;
        }
        return skillName.startsWith("combat_") || skillName.startsWith("outdoors_creaturehandler_") || skillName.startsWith("outdoors_squadleader_") || skillName.startsWith("force_discipline_");
    }
    public static boolean isPrecuBaseCombatSkillBox(String skillName) throws InterruptedException
    {
        return skillName != null && (skillName.startsWith("combat_brawler_") || skillName.startsWith("combat_marksman_"));
    }
    public static int getPrecuCombatSkillScore(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !player.isLoaded() || !isPlayer(player))
        {
            return 0;
        }
        String[] learnedSkills = getSkillListingForPlayer(player);
        if (learnedSkills == null || learnedSkills.length == 0)
        {
            return 0;
        }
        int score = 0;
        for (String learnedSkill : learnedSkills)
        {
            if (!isPrecuCombatSkillBox(learnedSkill))
            {
                continue;
            }
            int points = dataTableGetInt(TBL_SKILL, learnedSkill, "POINTS_REQUIRED");
            if (points <= 0)
            {
                continue;
            }
            if (!isPrecuBaseCombatSkillBox(learnedSkill))
            {
                points *= PRECU_ADVANCED_COMBAT_SKILL_WEIGHT;
            }
            score += points;
        }
        return Math.min(score, PRECU_COMBAT_SKILL_SCORE_MAX);
    }
    public static boolean isPrecuSkillBoxInFamilies(String skillName, String[] prefixes) throws InterruptedException
    {
        if (skillName == null || skillName.length() == 0 || skillName.indexOf("_prereq") >= 0 || prefixes == null)
        {
            return false;
        }
        for (String prefix : prefixes)
        {
            if (prefix != null && prefix.length() > 0 && skillName.startsWith(prefix))
            {
                return true;
            }
        }
        return false;
    }
    public static int getPrecuProfessionSkillScore(obj_id player, String[] professionPrefixes, String[] basePrefixes) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !player.isLoaded() || !isPlayer(player))
        {
            return 0;
        }
        String[] learnedSkills = getSkillListingForPlayer(player);
        if (learnedSkills == null || learnedSkills.length == 0)
        {
            return 0;
        }
        int score = 0;
        for (String learnedSkill : learnedSkills)
        {
            if (!isPrecuSkillBoxInFamilies(learnedSkill, professionPrefixes))
            {
                continue;
            }
            int points = dataTableGetInt(TBL_SKILL, learnedSkill, "POINTS_REQUIRED");
            if (points <= 0)
            {
                continue;
            }
            if (!isPrecuSkillBoxInFamilies(learnedSkill, basePrefixes))
            {
                points *= PRECU_ADVANCED_COMBAT_SKILL_WEIGHT;
            }
            score += points;
        }
        return Math.min(score, PRECU_COMBAT_SKILL_SCORE_MAX);
    }
    public static int getPrecuCraftingContentDifficulty(obj_id player) throws InterruptedException
    {
        String[] craftingPrefixes = { "crafting_" };
        String[] basePrefixes = { "crafting_artisan_" };
        return Math.max(1, getPrecuProfessionSkillScore(player, craftingPrefixes, basePrefixes));
    }
    public static int getPrecuEntertainerContentDifficulty(obj_id player) throws InterruptedException
    {
        String[] entertainerPrefixes = { "social_entertainer_", "social_dancer_", "social_musician_", "social_imagedesigner_" };
        String[] basePrefixes = { "social_entertainer_" };
        return Math.max(1, getPrecuProfessionSkillScore(player, entertainerPrefixes, basePrefixes));
    }
    public static int getPrecuEncounterDifficulty(obj_id player) throws InterruptedException
    {
        return Math.max(1, getPrecuCombatSkillScore(player));
    }
    public static int getPrecuGroupCombatDifficulty(obj_id playerOrGroup) throws InterruptedException
    {
        obj_id groupId = group.isGroupObject(playerOrGroup) ? playerOrGroup : getGroupObject(playerOrGroup);
        if (!isIdValid(groupId))
        {
            return getPrecuEncounterDifficulty(playerOrGroup);
        }
        obj_id[] members = getGroupMemberIds(groupId);
        if (members == null || members.length == 0)
        {
            return getPrecuEncounterDifficulty(playerOrGroup);
        }
        int highestScore = 0;
        float groupDifficulty = 0.0f;
        for (obj_id member : members)
        {
            if (!isIdValid(member) || !exists(member) || !member.isLoaded() || !isPlayer(member))
            {
                continue;
            }
            int memberScore = getPrecuCombatSkillScore(member);
            if (memberScore > highestScore)
            {
                groupDifficulty += memberScore - highestScore + (highestScore / 5.0f);
                highestScore = memberScore;
            }
            else
            {
                groupDifficulty += memberScore / 5.0f;
            }
        }
        return Math.max(1, Math.round(groupDifficulty));
    }
    public static int getGroupLevel(obj_id objPlayer) throws InterruptedException
    {
        return getPrecuGroupCombatDifficulty(objPlayer);
    }
    public static void checkForJediAbility(obj_id objPlayer, String strSkill, int intDelay) throws InterruptedException
    {
        if (!getEnableNewJediTracking())
        {
            LOG("jedi", "System disabled");
            return;
        }
        if (isJedi(objPlayer))
        {
            return;
        }
        if (hasObjVar(objPlayer, "jedi.timeStamp"))
        {
            removeObjVar(objPlayer, pclib.OBJVAR_JEDI_SKILL_REQUIREMENTS);
            return;
        }
        Vector strSkillsNeeded = getResizeableStringArrayObjVar(objPlayer, pclib.OBJVAR_JEDI_SKILL_REQUIREMENTS);
        if (strSkillsNeeded == null)
        {
            if (!hasJediSlot(objPlayer))
            {
                if (!hasObjVar(objPlayer, "jedi.timeStamp"))
                {
                    if (!hasObjVar(objPlayer, "jedi.postponeGrant"))
                    {
                        CustomerServiceLog("jedi", "CATASTROPHIC JEDI FAILURE, DATA IS MUNGED ON " + objPlayer);
                    }
                }
            }
            LOG("jedi", "no array");
            return;
        }
        if (strSkillsNeeded.size() == 0)
        {
            LOG("jedi", "Array is 0?");
            return;
        }
        int intElement = utils.getElementPositionInArray(strSkillsNeeded, strSkill);
        LOG("jedi", "checking for " + strSkill + " intIndex is " + intElement);
        if (intElement != -1)
        {
            strSkillsNeeded = utils.removeElementAt(strSkillsNeeded, intElement);
        }
        updateJediSkillRequirements(objPlayer, strSkillsNeeded);
    }
    public static void updateJediSkillRequirements(obj_id player, Vector skillsNeeded) throws InterruptedException
    {
        if (!isIdValid(player) || isJedi(player) || skillsNeeded == null)
        {
            return;
        }
        if (skillsNeeded.size() > 0)
        {
            setObjVar(player, pclib.OBJVAR_JEDI_SKILL_REQUIREMENTS, skillsNeeded);
            if (skillsNeeded.size() <= 4)
            {
                CustomerServiceLog("jedi", "CLOSE_PLAYER: " + getFirstName(player) + " (" + player + ") needs " + skillsNeeded.size() + " more skills to activate their FS slot.");
            }
            else if (skillsNeeded.size() <= 6)
            {
                CustomerServiceLog("jedi", getFirstName(player) + " (" + player + ") needs " + skillsNeeded.size() + " more skills to activate their FS slot.");
            }
        }
        else 
        {
            removeObjVar(player, pclib.OBJVAR_JEDI_SKILL_REQUIREMENTS);
            setObjVar(player, "jedi.timeStamp", 0);
            messageTo(player, "makeJedi", null, 0.1f, false);
        }
    }
    public static void setJediSkills(obj_id objPlayer) throws InterruptedException
    {
        if (!isJedi(objPlayer) && !hasJediSlot(objPlayer) && !hasObjVar(objPlayer, pclib.OBJVAR_JEDI_SKILL_REQUIREMENTS))
        {
            int numRequired = 0;
            String[] strSkills = dataTableGetStringColumn(JEDI_SKILL_REQUIREMENTS_DATATABLE, "strSkillList");
            numRequired = dataTableGetInt(JEDI_SKILL_REQUIREMENTS_DATATABLE, 0, "intNumRequired");
            debugServerConsoleMsg(null, "intNumRequired is " + numRequired);
            if (numRequired > strSkills.length)
            {
                debugServerConsoleMsg(null, "NO JEDI ENABLED, SKILLS REQUIRED GREATER THAN SKILLS IN LIST!");
                return;
            }
            int numAvailable = strSkills.length;
            String[] strSkillsNeeded = new String[numRequired];
            for (int i = 0; i < numRequired; ++i)
            {
                int randomNumber = rand(0, numAvailable - 1);
                strSkillsNeeded[i] = strSkills[randomNumber];
                System.arraycopy(strSkills, randomNumber + 1, strSkills, randomNumber, numAvailable - 1 - randomNumber);
                strSkills[--numAvailable] = null;
            }
            if (strSkillsNeeded.length > 0)
            {
                setObjVar(objPlayer, pclib.OBJVAR_JEDI_SKILL_REQUIREMENTS, strSkillsNeeded);
            }
            else 
            {
                CustomerServiceLog("jedi", "Failed to set jedi required skills for %TU", objPlayer);
            }
        }
    }
    public static void fixTerrainNegotiationMods(obj_id player) throws InterruptedException
    {
        if (hasObjVar(player, "_notskill.mods.slope_move"))
        {
            int mod = getIntObjVar(player, "_notskill.mods.slope_move");
            applySkillStatisticModifier(player, "slope_move", -mod);
        }
    }
    public static boolean noisyGrantSkill(obj_id player, String skillName) throws InterruptedException
    {
        if (grantSkillToPlayer(player, skillName))
        {
            sendSystemMessageProse(player, prose.getPackage(PROSE_SKILL_LEARNED, new string_id(SKILL_N, skillName)));
            return true;
        }
        else 
        {
            sendSystemMessageProse(player, prose.getPackage(PROSE_TRAIN_FAILED, new string_id(SKILL_N, skillName)));
            return false;
        }
    }
    public static void doPlayerLeveling(obj_id objPlayer, int intOldLevel, int intNewLevel) throws InterruptedException
    {
        // Publish 14.1 progression is skill-box based.  Combat-level changes must
        // not drive stat grants, level-up effects, or NGE tutorial UI.
    }
    public static void setPlayerStatsForLevel(obj_id objPlayer, int intLevel) throws InterruptedException
    {
        // Compatibility entry point retained for legacy scripts.  Pre-CU HAM
        // maxima and regeneration are authoritative and never level-derived.
    }
    public static int getPlayerStatForLevel(obj_id player, int intLevel, String statString) throws InterruptedException
    {
        // Compatibility entry point retained for later-era callers. Publish
        // 14.1 HAM and secondary statistics are not derived from combat level.
        return 0;
    }
    public static void sendlevelUpStatChangeSystemMessages(obj_id player, int oldCombatLevel, int newCombatLevel) throws InterruptedException
    {
        // Publish 14.1 clients receive skill-box acquisition feedback instead
        // of NGE combat-level stat-gain spam.
    }
    public static String getProfessionName(String strTemplate) throws InterruptedException
    {
        player_levels.skill_template_data professionData = player_levels.getSkillTemplateData(strTemplate);
        if (professionData == null)
        {
            return null;
        }
        return professionData.strClassName;
    }
    public static void recalcPlayerPools(obj_id objPlayer, boolean boolHealEverything) throws InterruptedException
    {
        if (!isPlayer(objPlayer))
        {
            return;
        }
        // Keep this compatibility hook for callers that request a heal, but do
        // not recalculate any pool from NGE profession or combat-level tables.
        if (boolHealEverything)
        {
            setAttrib(objPlayer, HEALTH, getMaxAttrib(objPlayer, HEALTH));
            setAttrib(objPlayer, ACTION, getMaxAttrib(objPlayer, ACTION));
            setAttrib(objPlayer, MIND, getMaxAttrib(objPlayer, MIND));
        }
    }
    public static void grantAllPoliticianSkills(obj_id player) throws InterruptedException
    {
        String[] skillNames = 
        {
            "social_politician_novice",
            "social_politician_fiscal_01",
            "social_politician_fiscal_02",
            "social_politician_fiscal_03",
            "social_politician_fiscal_04",
            "social_politician_martial_01",
            "social_politician_martial_02",
            "social_politician_martial_03",
            "social_politician_martial_04",
            "social_politician_civic_01",
            "social_politician_civic_02",
            "social_politician_civic_03",
            "social_politician_civic_04",
            "social_politician_urban_01",
            "social_politician_urban_02",
            "social_politician_urban_03",
            "social_politician_urban_04",
            "social_politician_master"
        };
        setObjVar(player, "clickRespec.granting", true);
        for (String skillName : skillNames) {
            if (!hasSkill(player, skillName)) {
                grantSkill(player, skillName);
            }
        }
        removeObjVar(player, "clickRespec.granting");
    }
    public static int getProfessionPhase(obj_id player) throws InterruptedException
    {
        int combatScore = getPrecuCombatSkillScore(player);
        if (combatScore >= PRECU_PHASE_FOUR_COMBAT_SCORE)
        {
            return PHASE_FOUR;
        }
        if (combatScore >= PRECU_PHASE_THREE_COMBAT_SCORE)
        {
            return PHASE_THREE;
        }
        if (combatScore >= PRECU_PHASE_TWO_COMBAT_SCORE)
        {
            return PHASE_TWO;
        }
        return PHASE_ONE;
    }
    public static boolean validateExpertise(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player))
        {
            return false;
        }
        String[] expertiseSkills = expertise.getExpertiseAllocation(player);
        if (expertiseSkills == null || expertiseSkills.length <= 0)
        {
            return false;
        }
        // Expertises are an NGE progression layer. Remove only the persisted
        // expertise allocation; the broad later-era reset also strips buffs
        // and mutates respec state that Publish 14.1 owns independently.
        resetExpertises(player);
        CustomerServiceLog(
            "precuExpertiseRetirement:",
            "Player " + getFirstName(player) + "(" + player +
            ") had " + expertiseSkills.length +
            " later-era expertise allocation(s) removed.");
        return false;
    }
}
