package script.npc.skillteacher;

import script.*;
import script.library.*;

import java.util.Vector;

public class skillteacher extends script.base_script
{
    public skillteacher()
    {
    }
    public static final String VAR_SKILL_TEMPLATE = "npcSkillTemplate";
    public static final String CONVONAME = "skill_teacher";
    public static final String CONVOFILE = "skill_teacher";
    public static final String JEDI_TRAINER = "jedi_trainer";
    public static final String JEDI_TRAINER_LIGHT = "jedi_trainer_light";
    public static final String JEDI_TRAINER_DARK = "jedi_trainer_dark";
    public static final String SKILL_N = "skl_n";
    public static final String SKILL_D = "skl_d";
    public static final String SKILL_T = "skl_t";
    public static final String SCRIPT_NPC_CONVERSE = "npc.converse.npc_converse_menu";
    public static final String FACETO_VOLUME_NAME = "faceToTriggerVolume";
    public static final string_id[] OPT_DEFAULT = 
    {
        new string_id(CONVOFILE, "opt1_1"),
        new string_id(CONVOFILE, "opt1_2")
    };
    public static final string_id[] OPT_YES_BACK = 
    {
        new string_id(CONVOFILE, "yes"),
        new string_id(CONVOFILE, "back")
    };
    public static final string_id[] OPT_YES_NO = 
    {
        new string_id(CONVOFILE, "yes"),
        new string_id(CONVOFILE, "no")
    };
    public static final String TBL = "datatables/skill/skills.iff";
    public static final string_id PROSE_NSF = new string_id(CONVOFILE, "prose_nsf");
    public static final string_id PROSE_PAY = new string_id(CONVOFILE, "prose_pay");
    public static final string_id PROSE_SKILL_LEARNED = new string_id(CONVOFILE, "prose_skill_learned");
    public static final string_id PROSE_TRAIN_FAILED = new string_id(CONVOFILE, "prose_train_failed");
    public static final string_id SID_TRAINING_COST_REFUNDED = new string_id(CONVOFILE, "training_cost_refunded");
    public static final string_id SID_ALREADY_HAVE_THIS_SKILL = new string_id(CONVOFILE, "already_have_this_skill");
    public static final string_id SID_DO_NOT_HAVE_SKILL = new string_id(CONVOFILE, "do_not_have_skill");
    public static final string_id SID_TRAIN_ELDER_SKILLS =
        new string_id(elder_skill.STRING_TABLE, "train_elder_skills");
    public static final string_id SID_ELDER_TRAINING_TITLE =
        new string_id(elder_skill.STRING_TABLE, "elder_training_title");
    private static final int ELDER_TRAINING_MENU = menu_info_types.SERVER_MENU7;
    private static final float ELDER_TRAINING_RANGE = 10.0f;
    private static final String ELDER_SUI_ROOT = "precu.elderTraining";
    private static final String ELDER_SUI_TRAINER = ELDER_SUI_ROOT + ".trainer";
    private static final String ELDER_SUI_SKILL = ELDER_SUI_ROOT + ".skill";
    private static final String ELDER_SUI_PID = ELDER_SUI_ROOT + ".pid";
    public static final int STATUS_UNKNOWN = -1;
    public static final int STATUS_NONE = 0;
    public static final int STATUS_LEARN = 1;
    public static final int STATUS_INFO = 2;
    private static final String PRECU_OP_ROOT = "precu.phaseA.operation";
    private static final String PRECU_OP_ATTEMPT_ID = PRECU_OP_ROOT + ".attemptId";
    private static final String PRECU_OP_ID = PRECU_OP_ROOT + ".id";
    private static final String PRECU_OP_KIND = PRECU_OP_ROOT + ".kind";
    private static final String PRECU_OP_STATE = PRECU_OP_ROOT + ".state";
    private static final String PRECU_OP_UPDATED = PRECU_OP_ROOT + ".updated";
    private static final String PRECU_OP_LIFECYCLE_ID = PRECU_OP_ROOT + ".lifecycleId";
    private static final String PRECU_OP_COST = PRECU_OP_ROOT + ".cost";
    private static final String PRECU_OP_TRAINER_OID = PRECU_OP_ROOT + ".trainerOid";
    private static final String PRECU_OP_SKILL_NAME = PRECU_OP_ROOT + ".skillName";
    private static final String PRECU_OP_PRE_CREDITS = PRECU_OP_ROOT + ".preCredits";
    private static final String PRECU_OP_PRE_CASH = PRECU_OP_ROOT + ".preCash";
    private static final String PRECU_OP_PRE_BANK = PRECU_OP_ROOT + ".preBank";
    private static final String PRECU_OP_PRE_XP = PRECU_OP_ROOT + ".preXp";
    private static final String PRECU_OP_PRE_POINTS = PRECU_OP_ROOT + ".prePoints";
    private static final String PRECU_OP_PRE_CAP = PRECU_OP_ROOT + ".preCap";
    private static final String PRECU_OP_PRE_NOVICE = PRECU_OP_ROOT + ".preNovice";
    private static final String PRECU_OP_PRE_SKILL = PRECU_OP_ROOT + ".preSkill";
    private static final String PRECU_OP_PROTOCOL_VERSION = PRECU_OP_ROOT + ".protocolVersion";
    private static final String PRECU_OP_REFUND_GENERATION =
        PRECU_OP_ROOT + ".refundGeneration";
    private static final String PRECU_OP_REFUND_ATTEMPT_KEY =
        PRECU_OP_ROOT + ".refundAttemptKey";
    private static final String PRECU_OP_REFUND_RETRY_CONSUMED =
        PRECU_OP_ROOT + ".refundRetryConsumed";
    private static final String PRECU_OP_ACCOUNTING_ATTEMPT_KEY =
        PRECU_OP_ROOT + ".accountingAttemptKey";
    private static final String PRECU_OP_ACCOUNTING_ACCOUNT =
        PRECU_OP_ROOT + ".accountingAccount";
    private static final String PRECU_OP_ACCOUNTING_OUTCOME =
        PRECU_OP_ROOT + ".accountingOutcome";
    private static final String PRECU_PARAM_ID = "precuPhaseAOperationId";
    private static final String PRECU_PARAM_KIND = "precuPhaseAOperationKind";
    private static final String PRECU_REFUND_PARAM_GENERATION =
        "precuPhaseARefundGeneration";
    private static final String PRECU_REFUND_PARAM_ATTEMPT_KEY =
        "precuPhaseARefundAttemptKey";
    private static final String PRECU_REFUND_PARAM_RETRY_CONSUMED =
        "precuPhaseARefundRetryConsumed";
    private static final String PRECU_ACCOUNTING_PARAM_ATTEMPT_KEY =
        "precuPhaseAAccountingAttemptKey";
    private static final String PRECU_PROTOCOL_PARAM_VERSION =
        "precuPhaseAProtocolVersion";
    private static final String PRECU_LIFECYCLE_ATTEMPT_ID = "precu.phaseA.lifecycle.attemptId";
    private static final String PRECU_LIFECYCLE_ID = "precu.phaseA.lifecycle.id";
    private static final String PRECU_LIFECYCLE_STATE = "precu.phaseA.lifecycle.state";
    private static final String PRECU_LIFECYCLE_BASE_CASH = "precu.phaseA.lifecycle.baseCash";
    private static final String PRECU_LIFECYCLE_BASE_BANK = "precu.phaseA.lifecycle.baseBank";
    private static final String PRECU_LIFECYCLE_BASE_XP = "precu.phaseA.lifecycle.baseXp";
    private static final String PRECU_LIFECYCLE_BASE_POINTS = "precu.phaseA.lifecycle.basePoints";
    private static final String PRECU_LIFECYCLE_BASE_CAP = "precu.phaseA.lifecycle.baseCap";
    private static final String PRECU_LIFECYCLE_BASE_NOVICE = "precu.phaseA.lifecycle.baseNovice";
    private static final String PRECU_LIFECYCLE_BASE_SKILL = "precu.phaseA.lifecycle.baseSkill";
    private static final String PRECU_LIFECYCLE_PARAM_ID = "precuPhaseALifecycleId";
    private static final String PRECU_RELOG_NONCE = "precu.phaseA.relogNonce";
    private static final String PRECU_RESTART_NONCE = "precu.phaseA.restartNonce";
    private static final String PRECU_CRAFTING_XP_TYPE = "crafting_general";
    private static final String PRECU_CRAFTING_NOVICE_SKILL = "crafting_artisan_novice";
    private static final String PRECU_CRAFTING_SKILL = "crafting_artisan_engineering_01";
    private static final int PRECU_CRAFTING_TRAINER_COST = 1000;
    private static final int PRECU_CRAFTING_XP_COST = 500;
    private static final int PRECU_PREPURCHASE_XP_CAP = 1000;
    private static final int PRECU_TRAINED_XP_CAP = 2000;
    private static final int PRECU_CRAFTING_SCHEMATIC_COUNT = 35;
    private static final int PRECU_PROTOCOL_VERSION = 64;
    private static final String PRECU_ACCOUNTING_OUTCOME_NONE = "none";
    private static final String PRECU_ACCOUNTING_OUTCOME_REQUEST_QUEUE_FAILED =
        "REQUEST_QUEUE_FAILED";
    private static final String PRECU_VECTOR_PRE = "PRE";
    private static final String PRECU_VECTOR_DEBIT = "DEBIT";
    private static final String PRECU_VECTOR_HELD = "HELD";
    private static final String PRECU_VECTOR_REFUND = "REFUND";
    private static final String PRECU_SCHEMATIC_GROUP_TABLE =
        "datatables/crafting/schematic_group.iff";
    private static final String[] PRECU_CRAFTING_VECTOR_COMMANDS =
    {
        "private_artisan_novice",
        "sample",
        "survey",
        "private_artisan_engineering_1"
    };
    private static final String[] PRECU_CRAFTING_VECTOR_MODS =
    {
        "surveying",
        "general_assembly",
        "general_experimentation",
        "clothing_customization",
        "armor_customization",
        "slope_move"
    };
    private static final int[] PRECU_CRAFTING_VECTOR_MOD_VALUES =
    {
        20,
        30,
        30,
        20,
        40,
        25
    };
    private static final int[] PRECU_CRAFTING_PURCHASE_MOD_DELTAS =
    {
        0,
        10,
        10,
        0,
        20,
        0
    };
    private static final String[] PRECU_CRAFTING_VECTOR_SCHEMATIC_GROUPS =
    {
        "craftArtisanNewbieGroupA",
        "craftArtisanNewbieGroupB",
        "craftArtisanSurveyGroupA",
        "craftArtisanEngineeringGroupA",
        "craftArtisanToolGroupA"
    };
    private static final String[] PRECU_CRAFTING_PURCHASE_SCHEMATICS =
    {
        "object/draft_schematic/item/craftable_bug_habitat.iff",
        "object/draft_schematic/item/item_battery_droid.iff",
        "object/draft_schematic/item/item_clothing_tool.iff",
        "object/draft_schematic/item/item_firework_five.iff",
        "object/draft_schematic/item/item_firework_four.iff",
        "object/draft_schematic/item/item_hundred_sided_dice.iff",
        "object/draft_schematic/item/item_space_tool.iff",
        "object/draft_schematic/item/item_structure_tool.iff",
        "object/draft_schematic/item/item_twelve_sided_dice.iff",
        "object/draft_schematic/item/item_twenty_sided_dice.iff",
        "object/draft_schematic/item/item_weapon_tool.iff"
    };
    private static final String PRECU_BUILD_FINGERPRINT = "__PHASE_A_BUILD_FINGERPRINT__";
    public static String getPrecuPhaseABuildFingerprint()
    {
        return PRECU_BUILD_FINGERPRINT;
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        String teacherType = getStringObjVar(self, "trainer");
        if (teacherType != null)
        {
            if (teacherType.equals("trainer_shipwright"))
            {
                setCondition(self, CONDITION_CONVERSABLE);
                setCondition(self, CONDITION_SPACE_INTERESTING);
            }
            else 
            {
                setCondition(self, CONDITION_CONVERSABLE);
            }
        }
        createTriggerVolume(FACETO_VOLUME_NAME, 8.0f, true);
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        int mnu = mi.addRootMenu(menu_info_types.CONVERSE_START, null);
        menu_info_data mdata = mi.getMenuItemById(mnu);
        mdata.setServerNotify(false);
        String trainerType = hasObjVar(self, "trainer") ?
            getStringObjVar(self, "trainer") : "";
        if (elder_skill.isAdvancedProfessionTrainer(self) &&
            (!"trainer_shipwright".equals(trainerType) ||
                features.isSpaceEdition(player)))
        {
            mi.addRootMenu(ELDER_TRAINING_MENU, SID_TRAIN_ELDER_SKILLS);
        }
        setCondition(self, CONDITION_CONVERSABLE);
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuSelect(obj_id self, obj_id player, int item)
        throws InterruptedException
    {
        if (item != ELDER_TRAINING_MENU ||
            !elder_skill.isAdvancedProfessionTrainer(self) ||
            !isIdValid(player) || !isPlayer(player))
        {
            return SCRIPT_CONTINUE;
        }
        String trainerType = hasObjVar(self, "trainer") ?
            getStringObjVar(self, "trainer") : "";
        if ("trainer_shipwright".equals(trainerType) &&
            !features.isSpaceEdition(player))
        {
            return SCRIPT_CONTINUE;
        }
        String elderSkill = elder_skill.getElderSkillForTrainer(self);
        String masterSkill = elder_skill.getMasterSkillForElder(elderSkill);
        float distance = getDistance(self, player);
        if (elderSkill == null || masterSkill == null || distance < 0.0f ||
            distance > ELDER_TRAINING_RANGE)
        {
            sendSystemMessage(
                player,
                new string_id(elder_skill.STRING_TABLE, "trainer_too_far"));
            return SCRIPT_CONTINUE;
        }
        if (!hasSkill(player, masterSkill))
        {
            sendSystemMessage(
                player,
                new string_id(elder_skill.STRING_TABLE, "requires_master"));
            return SCRIPT_CONTINUE;
        }

        boolean renewal = hasSkill(player, elderSkill);
        String prompt = "@" + new string_id(
            elder_skill.STRING_TABLE,
            renewal ? "elder_renewal_prompt" : "elder_training_prompt");

        clearElderTrainingContext(player);
        utils.setScriptVar(player, ELDER_SUI_TRAINER, self);
        utils.setScriptVar(player, ELDER_SUI_SKILL, elderSkill);
        int pid = sui.msgbox(
            self,
            player,
            prompt,
            sui.YES_NO,
            "@" + SID_ELDER_TRAINING_TITLE,
            "handleElderTrainingConfirmation");
        utils.setScriptVar(player, ELDER_SUI_PID, pid);
        return SCRIPT_CONTINUE;
    }
    public int handleElderTrainingConfirmation(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        if (!isIdValid(player) || !isPlayer(player) ||
            !utils.hasScriptVar(player, ELDER_SUI_TRAINER) ||
            !utils.hasScriptVar(player, ELDER_SUI_SKILL) ||
            !utils.hasScriptVar(player, ELDER_SUI_PID))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id expectedTrainer =
            utils.getObjIdScriptVar(player, ELDER_SUI_TRAINER);
        String elderSkill =
            utils.getStringScriptVar(player, ELDER_SUI_SKILL);
        int expectedPid = utils.getIntScriptVar(player, ELDER_SUI_PID);
        int pageId = params.getInt("pageId");
        int button = sui.getIntButtonPressed(params);
        clearElderTrainingContext(player);

        String currentSkill = elder_skill.getElderSkillForTrainer(self);
        String trainerType = hasObjVar(self, "trainer") ?
            getStringObjVar(self, "trainer") : "";
        float distance = getDistance(self, player);
        if (button == sui.BP_CANCEL || expectedTrainer != self ||
            pageId != expectedPid || elderSkill == null ||
            !elderSkill.equals(currentSkill) ||
            !elder_skill.isAdvancedProfessionTrainer(self) ||
            ("trainer_shipwright".equals(trainerType) &&
                !features.isSpaceEdition(player)) ||
            distance < 0.0f || distance > ELDER_TRAINING_RANGE)
        {
            return SCRIPT_CONTINUE;
        }

        int result = elder_skill.trainOrRenew(player, elderSkill);
        String messageKey;
        switch (result)
        {
            case 1:
                messageKey = "elder_trained";
                break;
            case 2:
                messageKey = "elder_renewed";
                break;
            case -2:
                messageKey = "requires_master";
                break;
            case -4:
                messageKey = "insufficient_apprenticeship_xp";
                break;
            default:
                messageKey = "elder_training_failed";
                break;
        }
        sendSystemMessage(
            player,
            new string_id(elder_skill.STRING_TABLE, messageKey));
        return SCRIPT_CONTINUE;
    }
    private void clearElderTrainingContext(obj_id player)
        throws InterruptedException
    {
        if (isIdValid(player))
        {
            utils.removeScriptVarTree(player, ELDER_SUI_ROOT);
        }
    }
    public int OnIncapacitated(obj_id self, obj_id killer) throws InterruptedException
    {
        clearCondition(self, CONDITION_CONVERSABLE);
        clearCondition(self, CONDITION_SPACE_INTERESTING);
        detachScript(self, "npc.skillteacher.skillteacher");
        return SCRIPT_CONTINUE;
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        if (!utils.hasScriptVar(self, skill.SCRIPTVAR_SKILLS))
        {
            String tbl_trainer_skills = "datatables/npc_customization/skill_table.iff";
            String teacherType = getStringObjVar(self, "trainer");
            String[] skillList = dataTableGetStringColumnNoDefaults(tbl_trainer_skills, teacherType);
            if (teacherType != null && (!teacherType.equals("") && teacherType.equals("trainer_creaturehandler")))
            {
                if (!hasScript(self, "systems.pet_tradein.pet_tradein"))
                {
                    attachScript(self, "systems.pet_tradein.pet_tradein");
                }
            }
            if (skillList == null || skillList.length == 0)
            {
                detachScript(self, "npc.skillteacher.skillteacher");
                return SCRIPT_OVERRIDE;
            }
            else 
            {
                if (teacherType != null)
                {
                    if (teacherType.equals("trainer_shipwright"))
                    {
                        setCondition(self, CONDITION_SPACE_INTERESTING);
                        setCondition(self, CONDITION_CONVERSABLE);
                    }
                    else 
                    {
                        setCondition(self, CONDITION_CONVERSABLE);
                    }
                }
                utils.setBatchScriptVar(self, skill.SCRIPTVAR_SKILLS, skillList);
            }
            skillList = dataTableGetStringColumnNoDefaults(tbl_trainer_skills, "trainer_jedi");
            if (skillList != null && skillList.length > 0)
            {
                utils.setBatchScriptVar(self, skill.SCRIPTVAR_JEDI_SKILLS, skillList);
            }
        }
        createTriggerVolume(FACETO_VOLUME_NAME, 8.0f, true);
        return SCRIPT_CONTINUE;
    }
    public int OnStartNpcConversation(obj_id self, obj_id speaker) throws InterruptedException
    {
        if (hasObjVar(speaker, "jedi.usingSui"))
        {
            string_id strSpam = new string_id("jedi_spam", "cant_train_while_converting");
            chat.chat(self, speaker, strSpam, chat.ChatFlag_targetOnly);
            return SCRIPT_CONTINUE;
        }
        int city_id = getCityAtLocation(getLocation(self), 0);
        if (cityExists(city_id) && city.isCityBanned(speaker, city_id))
        {
            sendSystemMessage(speaker, new string_id("city/city", "city_banned"));
            return SCRIPT_CONTINUE;
        }
        String trainerType = "trainer_unknown";
        if (hasObjVar(self, "trainer"))
        {
            trainerType = getStringObjVar(self, "trainer");
        }
        if (trainerType.equals("trainer_shipwright") && !features.isSpaceEdition(speaker))
        {
            doAnimationAction(self, "thumbs_down");
            sendSystemMessage(speaker, new string_id("skill_teacher", "requires_jtl"));
            chat.publicChat(self, speaker, new string_id("skill_teacher", "too_complicated"));
            return SCRIPT_CONTINUE;
        }
        faceTo(self, speaker);
        if (!checkSkillStatus(self, speaker))
        {
            return SCRIPT_CONTINUE;
        }
        if (jedi.isJediTrainerForPlayer(speaker, self))
        {
            if (hasObjVar(speaker, "jedi.intFindNewTrainer"))
            {
                messageTo(speaker, "findNewTrainer", null, 0, false);
                string_id strSpam = new string_id("jedi_spam", "not_your_trainer");
                chat.chat(self, speaker, strSpam, chat.ChatFlag_targetOnly);
                return SCRIPT_CONTINUE;
            }
        }
        string_id msg = new string_id(CONVOFILE, trainerType);
        if (isJedi(speaker) && jedi.isJediTrainerForPlayer(speaker, self))
        {
            String jedi_convo = getJediConvoFile(speaker);
            if (jedi_convo != null && !jedi_convo.equals(""))
            {
                msg = new string_id(jedi_convo, "greeting");
            }
            else 
            {
                msg = new string_id(JEDI_TRAINER, "greeting");
            }
        }
        npcStartConversation(speaker, self, CONVONAME, msg, OPT_DEFAULT);
        return SCRIPT_CONTINUE;
    }
    public int OnEndNpcConversation(obj_id self, obj_id speaker) throws InterruptedException
    {
        utils.removeScriptVar(speaker, self.toString());
        removeObjVar(self, "confirmTeach." + speaker);
        return SCRIPT_CONTINUE;
    }
    public int OnNpcConversationResponse(obj_id self, String convoName, obj_id speaker, string_id sid_response) throws InterruptedException
    {
        String trainerType = getStringObjVar(self, "trainer");
        if (trainerType != null)
        {
            if (trainerType.equals("trainer_shipwright") && !features.isSpaceEdition(speaker))
            {
                doAnimationAction(self, "thumbs_down");
                sendSystemMessage(speaker, new string_id("skill_teacher", "requires_jtl"));
                chat.publicChat(self, speaker, new string_id("skill_teacher", "too_complicated"));
                return SCRIPT_CONTINUE;
            }
        }
        if (!convoName.equals(CONVONAME) && !convoName.equals(JEDI_TRAINER))
        {
            return SCRIPT_CONTINUE;
        }
        if (!checkSkillStatus(self, speaker))
        {
            return SCRIPT_CONTINUE;
        }
        String tbl = sid_response.getTable();
        String response = sid_response.getAsciiId();
        int status = utils.getIntScriptVar(speaker, self.toString());
        boolean checkArray = true;
        String convo = CONVOFILE;
        if (isJedi(speaker) && jedi.isJediTrainerForPlayer(speaker, self))
        {
            String jedi_convo = getJediConvoFile(speaker);
            if (jedi_convo != null && !jedi_convo.equals(""))
            {
                convo = jedi_convo;
            }
        }
        if (tbl.equals(SKILL_N))
        {
            string_id msg = new string_id(CONVOFILE, "msg3_1");
            boolean skillGranted = false;
            switch (status)
            {
                case STATUS_LEARN:
                String[] qualifiedSkills = skill.getQualifiedTeachableSkills(speaker, self);
                if (qualifiedSkills == null)
                {
                }
                else 
                {
                    if (utils.getElementPositionInArray(qualifiedSkills, response) > -1)
                    {
                        string_id sid_skillName = new string_id(SKILL_N, response);
                        int skillRow = dataTableSearchColumnForString(response, "NAME", skill.TBL_SKILL);
                        if (skillRow < 0)
                        {
                            return SCRIPT_CONTINUE;
                        }
                        int cost = dataTableGetInt(skill.TBL_SKILL, skillRow, "MONEY_REQUIRED");
                        if (cost < 0)
                        {
                            return SCRIPT_CONTINUE;
                        }
                        float skillMod = getEnhancedSkillStatisticModifier(speaker, "force_persuade");
                        skillMod = skillMod * 0.01f;
                        float discount = cost * skillMod;
                        cost = cost - (int)discount;
                        boolean newbieTraining = hasObjVar(speaker, "newbie.hasSkill");
                        if (hasObjVar(speaker, "newbie.trained"))
                        {
                            newbieTraining = false;
                        }
                        if (cost > 0 && (!newbieTraining))
                        {
                            int totalMoney = getTotalMoney(speaker);
                            if (totalMoney < cost)
                            {
                                prose_package pp = prose.getPackage(PROSE_NSF, sid_skillName, cost);
                                sendSystemMessageProse(speaker, pp);
                            }
                            else 
                            {
                                int ptsLeft = skill.getAvailableSkillPoints(speaker);
                                int ptsCost = skill.getSkillPointCost(response);
                                if (ptsLeft < ptsCost)
                                {
                                    int diff = ptsCost - ptsLeft;
                                    string_id PROSE_NSF_SKILL_PTS = new string_id(convo, "nsf_skill_pts");
                                    prose_package ppNsfSkillPts = prose.getPackage(PROSE_NSF_SKILL_PTS, sid_skillName, diff);
                                    npcSpeak(speaker, ppNsfSkillPts);
                                    npcSetConversationResponses(speaker, OPT_DEFAULT);
                                    return SCRIPT_CONTINUE;
                                }
                                String ovPath = "confirmTeach." + speaker;
                                setObjVar(self, ovPath + ".sid_skillname", sid_skillName);
                                setObjVar(self, ovPath + ".cost", cost);
                                string_id PROSE_COST = new string_id(convo, "prose_cost");
                                prose_package ppConfirm = prose.getPackage(PROSE_COST, sid_skillName, cost);
                                npcSpeak(speaker, ppConfirm);
                                npcSetConversationResponses(speaker, OPT_YES_NO);
                                return SCRIPT_CONTINUE;
                            }
                            npcSpeak(speaker, new string_id(convo, "msg1_1"));
                            npcSetConversationResponses(speaker, OPT_DEFAULT);
                            return SCRIPT_CONTINUE;
                        }
                        else 
                        {
                            int ptsLeft = skill.getAvailableSkillPoints(speaker);
                            int ptsCost = skill.getSkillPointCost(response);
                            if (ptsLeft < ptsCost)
                            {
                                int diff = ptsCost - ptsLeft;
                                string_id PROSE_NSF_SKILL_PTS = new string_id(convo, "nsf_skill_pts");
                                prose_package ppNsfSkillPts = prose.getPackage(PROSE_NSF_SKILL_PTS, sid_skillName, diff);
                                npcSpeak(speaker, ppNsfSkillPts);
                                npcSetConversationResponses(speaker, OPT_DEFAULT);
                                return SCRIPT_CONTINUE;
                            }
                            if (completeSkillPurchase(speaker, response))
                            {
                                if (response.equals("jedi_light_side_journeyman_novice") || response.equals("jedi_dark_side_journeyman_novice"))
                                {
                                    npcSpeak(speaker, new string_id(JEDI_TRAINER, "chosen_path"));
                                    npcEndConversation(speaker);
                                    setObjVar(speaker, "jedi.intFindNewTrainer", 1);
                                    return SCRIPT_CONTINUE;
                                }
                                msg = new string_id(convo, "msg3_2");
                                doAnimationAction(self, anims.PLAYER_FC_WINK);
                                if (response.equals("combat_bountyhunter_novice"))
                                {
                                    dictionary dctParams = new dictionary();
                                    dctParams.put("eventName", "BountyHunterNoviceGranted");
                                    messageTo(speaker, "handleHolocronEvent", dctParams, 0, false);
                                }
                                skillGranted = true;
                                if (hasObjVar(speaker, "newbie.hasSkill"))
                                {
                                    setObjVar(speaker, "newbie.trained", true);
                                }
                                if (hasSurpassedTrainer(self, speaker))
                                {
                                    prose_package ppFarewell = prose.getPackage(new string_id(convo, "surpass_trainer"), speaker);
                                    String chatType = chat.getChatType(self);
                                    String moodType = chat.getChatMood(self);
                                    npcSpeak(speaker, ppFarewell);
                                    npcEndConversation(speaker);
                                    return SCRIPT_CONTINUE;
                                }
                            }
                            else 
                            {
                                msg = new string_id(convo, "error_grant_skill");
                                doAnimationAction(self, anims.PLAYER_SHRUG_HANDS);
                            }
                        }
                    }
                }
                break;
                case STATUS_INFO:
                msg = new string_id(CONVOFILE, "msg3_3");
                String[] skillData = getSkillData(response, speaker);
                if ((skillData != null) && (skillData.length > 0))
                {
                    if (utils.getElementPositionInArray(getSkillListingForPlayer(speaker), response) > -1)
                    {
                        sui.listbox(speaker, utils.packStringId(SID_ALREADY_HAVE_THIS_SKILL), "@" + tbl + ":" + response, skillData);
                    }
                    else 
                    {
                        prose_package ppDoNotHaveSkill = prose.getPackage(SID_DO_NOT_HAVE_SKILL);
                        prose.setTO(ppDoNotHaveSkill, new string_id("skl_d", response));
                        String prompt = " \0" + packOutOfBandProsePackage(null, ppDoNotHaveSkill);
                        sui.listbox(speaker, prompt, "@" + tbl + ":" + response, skillData);
                    }
                }
                break;
                default:
                break;
            }
            npcSpeak(speaker, msg);
            npcSetConversationResponses(speaker, OPT_DEFAULT);
            return SCRIPT_CONTINUE;
        }
        else 
        {
            Vector opt = utils.concatArrays(null, OPT_DEFAULT);
            String[] skills = null;
            string_id msg = new string_id(convo, "msg2_1");
            switch (response) {
                case "opt1_1":
                    msg = new string_id(convo, "msg2_1");
                    skills = skill.getQualifiedTeachableSkills(speaker, self);
                    utils.setScriptVar(speaker, self.toString(), STATUS_LEARN);
                    break;
                case "opt1_2":
                    msg = new string_id(convoName, "msg2_2");
                    skills = skill.getTeachableSkills(speaker, self);
                    utils.setScriptVar(speaker, self.toString(), STATUS_INFO);
                    break;
                case "yes":
                    String ovPath = "confirmTeach." + speaker;
                    if (hasObjVar(self, ovPath)) {
                        string_id sid_skillName = getStringIdObjVar(self, ovPath + ".sid_skillname");
                        int cost = getIntObjVar(self, ovPath + ".cost");
                        if (sid_skillName != null && cost > 0) {
                            prose_package pp = prose.getPackage(PROSE_PAY, sid_skillName, cost);
                            sendSystemMessageProse(speaker, pp);
                            dictionary d = new dictionary();
                            d.put("skillName", sid_skillName.getAsciiId());
                            money.requestPayment(speaker, self, cost, "attemptedPayment", d, true);
                        }
                        removeObjVar(self, ovPath);
                    }
                    msg = new string_id(convo, "msg_yes");
                    checkArray = false;
                    utils.removeScriptVar(speaker, self.toString());
                    break;
                case "no":
                    msg = new string_id(convo, "msg_no");
                    checkArray = false;
                    utils.removeScriptVar(speaker, self.toString());
                    break;
                default:
                    checkArray = false;
                    utils.removeScriptVar(speaker, self.toString());
                    break;
            }
            if ((checkArray) && ((skills == null) || (skills.length == 0)))
            {
                msg = new string_id(convo, "error_empty_category");
            }
            else if (!checkArray)
            {
            }
            else 
            {
                opt.clear();
                for (String skill : skills) {
                    opt = utils.addElement(opt, new string_id(SKILL_N, skill));
                }
                opt = utils.addElement(opt, new string_id(CONVOFILE, "back"));
            }
            npcSpeak(speaker, msg);
            npcSetConversationResponses(speaker, opt);
            return SCRIPT_CONTINUE;
        }
    }
    public int attemptedPayment(obj_id self, dictionary params) throws InterruptedException
    {
        if ((params == null) || (params.isEmpty()))
        {
            return SCRIPT_CONTINUE;
        }
        boolean phaseATagged = hasAnyPhaseATag(params);
        // attemptedPayment is the normalized edge of the native payment
        // protocol. Tagged callbacks must carry both exact handler names and
        // an explicit, recognized return code; default dictionary values are
        // never interpreted as success.
        if (phaseATagged && !hasExactAttemptedPaymentEnvelope(params))
        {
            return SCRIPT_CONTINUE;
        }
        int retCode = money.getReturnCode(params);
        obj_id player = params.getObjId(money.DICT_PLAYER_ID);
        String callbackState = retCode == money.RET_SUCCESS ?
            "paymentSucceededCallback" : "paymentFailedCallback";
        boolean phaseAOperation = phaseATagged &&
            isExactActivePhaseAOperation(
                self,
                player,
                params,
                callbackState,
                retCode == money.RET_FAIL ? "enqueueing" : "",
                retCode == money.RET_SUCCESS ? PRECU_VECTOR_DEBIT : PRECU_VECTOR_PRE);
        // Any Phase-A tag creates a closed callback protocol. Quarantine
        // partial, stale, mismatched, and terminal replays before side effects.
        if (phaseATagged && !phaseAOperation)
        {
            return SCRIPT_CONTINUE;
        }
        if (retCode != money.RET_SUCCESS)
        {
            if (phaseAOperation)
            {
                if (!transitionPhaseAOperation(
                        self,
                        player,
                        params,
                        "paymentFailedCallback",
                        "enqueueing",
                        "paymentFailed",
                        PRECU_VECTOR_PRE))
                {
                    return SCRIPT_CONTINUE;
                }
            }
            return SCRIPT_CONTINUE;
        }
        if (!isIdValid(player) || (!isPlayer(player)))
        {
            return SCRIPT_CONTINUE;
        }
        String skillName = params.getString("skillName");
        if ((skillName == null) || (skillName.equals("")))
        {
            return SCRIPT_CONTINUE;
        }
        int cost = params.getInt(money.DICT_TOTAL);
        if (phaseAOperation && !transitionPhaseAOperation(
                self,
                player,
                params,
                "paymentSucceededCallback",
                "",
                "purchaseApplying",
                PRECU_VECTOR_DEBIT))
        {
            return SCRIPT_CONTINUE;
        }
        if (completeSkillPurchase(player, skillName))
        {
            if (phaseAOperation)
            {
                // The grant is HELD until the player-owned accounting protocol
                // receives an authoritative native success callback.  A
                // checked message only requests dispatch; it is not settlement.
                utils.setScriptVar(player, PRECU_RELOG_NONCE, params.getString(PRECU_PARAM_ID));
                if (!claimPhaseAAccountingRequest(self, player, params))
                {
                    return SCRIPT_CONTINUE;
                }
                params.put(
                    PRECU_ACCOUNTING_PARAM_ATTEMPT_KEY,
                    getStringObjVar(player, PRECU_OP_ACCOUNTING_ATTEMPT_KEY));
                params.put(money.DICT_ACCT_NAME, money.ACCT_SKILL_TRAINING);
                boolean requestQueued = messageTo(
                    player,
                    "precuPhaseARequestAccounting",
                    params,
                    0,
                    true);
                if (!requestQueued)
                {
                    publishPhaseAAccountingRequestQueueFailure(self, player, params);
                }
            }
            else
            {
                money.bankTo(self, money.ACCT_SKILL_TRAINING, cost);
            }
        }
        else 
        {
            if (phaseAOperation)
            {
                if (!claimInitialPhaseARefund(self, player, params))
                {
                    return SCRIPT_CONTINUE;
                }
                dispatchClaimedPhaseARefund(self, player, params, 1);
            }
            else
            {
                prose_package ppCostRefunded = prose.getPackage(SID_TRAINING_COST_REFUNDED);
                prose.setDI(ppCostRefunded, cost);
                sendSystemMessageProse(player, ppCostRefunded);
                money.bankTo(self, player, cost);
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int precuPhaseARefundSucceeded(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = params == null ? obj_id.NULL_ID : params.getObjId(money.DICT_PLAYER_ID);
        int generation = params == null ? 0 : params.getInt(PRECU_REFUND_PARAM_GENERATION);
        String family = generation == 1 ? "refundInitial" : "refundRecovery";
        if (hasAnyPhaseATag(params) && (generation == 1 || generation == 2) &&
            transitionExactPhaseARefund(
                self,
                player,
                params,
                family + "Dispatching",
                family + "Pending",
                "purchaseRefunded",
                PRECU_VECTOR_REFUND,
                generation))
        {
            prose_package ppCostRefunded = prose.getPackage(SID_TRAINING_COST_REFUNDED);
            prose.setDI(ppCostRefunded, params.getInt(money.DICT_TOTAL));
            sendSystemMessageProse(player, ppCostRefunded);
        }
        return SCRIPT_CONTINUE;
    }
    public int precuPhaseARefundFailed(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = params == null ? obj_id.NULL_ID : params.getObjId(money.DICT_PLAYER_ID);
        int generation = params == null ? 0 : params.getInt(PRECU_REFUND_PARAM_GENERATION);
        String family = generation == 1 ? "refundInitial" : "refundRecovery";
        if (hasAnyPhaseATag(params) && (generation == 1 || generation == 2))
        {
            transitionExactPhaseARefund(
                self,
                player,
                params,
                family + "Dispatching",
                family + "Pending",
                family + "Failed",
                PRECU_VECTOR_DEBIT,
                generation);
        }
        return SCRIPT_CONTINUE;
    }
    public int precuPhaseAResumeRefund(obj_id self, dictionary params)
        throws InterruptedException
    {
        obj_id player = params == null ? obj_id.NULL_ID :
            params.getObjId(money.DICT_PLAYER_ID);
        int generation = params == null ? 0 :
            params.getInt(PRECU_REFUND_PARAM_GENERATION);
        if (generation == 1 || generation == 2)
        {
            dispatchClaimedPhaseARefund(self, player, params, generation);
        }
        return SCRIPT_CONTINUE;
    }
    private boolean hasExactAttemptedPaymentEnvelope(dictionary params)
    {
        if (params == null || params.isEmpty() ||
            !params.containsKey(money.DICT_HANDLER) ||
            !params.containsKey(money.DICT_PAY_HANDLER) ||
            !params.containsKey(money.DICT_CODE) ||
            !params.containsKey(PRECU_PROTOCOL_PARAM_VERSION) ||
            !"attemptedPayment".equals(params.getString(money.DICT_HANDLER)) ||
            !"attemptedPayment".equals(params.getString(money.DICT_PAY_HANDLER)) ||
            params.getInt(PRECU_PROTOCOL_PARAM_VERSION) != PRECU_PROTOCOL_VERSION)
        {
            return false;
        }
        int code = params.getInt(money.DICT_CODE);
        return code == money.RET_SUCCESS || code == money.RET_FAIL;
    }
    private boolean hasExactSuccessfulPaymentProvenance(dictionary params)
    {
        return hasExactAttemptedPaymentEnvelope(params) &&
            params.getInt(money.DICT_CODE) == money.RET_SUCCESS;
    }
    private String buildPhaseAAttemptKey(String operationId, String kind, int generation)
    {
        return operationId + "." + kind + "." + generation;
    }
    private boolean claimPhaseAAccountingRequest(
        obj_id trainer,
        obj_id player,
        dictionary params) throws InterruptedException
    {
        if (!hasExactSuccessfulPaymentProvenance(params) ||
            !isExactActivePhaseAOperation(
                trainer, player, params, "purchaseApplying", "", PRECU_VECTOR_HELD) ||
            getIntObjVar(player, PRECU_OP_PROTOCOL_VERSION) != PRECU_PROTOCOL_VERSION ||
            getIntObjVar(player, PRECU_OP_REFUND_GENERATION) != 0 ||
            !"none".equals(getStringObjVar(player, PRECU_OP_REFUND_ATTEMPT_KEY)) ||
            getIntObjVar(player, PRECU_OP_REFUND_RETRY_CONSUMED) != 0 ||
            !"none".equals(getStringObjVar(player, PRECU_OP_ACCOUNTING_ATTEMPT_KEY)) ||
            !"none".equals(getStringObjVar(player, PRECU_OP_ACCOUNTING_ACCOUNT)) ||
            !PRECU_ACCOUNTING_OUTCOME_NONE.equals(
                getStringObjVar(player, PRECU_OP_ACCOUNTING_OUTCOME)))
        {
            return false;
        }
        String operationId = params.getString(PRECU_PARAM_ID);
        String attemptKey = buildPhaseAAttemptKey(operationId, "accounting", 1);
        setObjVar(player, PRECU_OP_ACCOUNTING_ATTEMPT_KEY, attemptKey);
        setObjVar(player, PRECU_OP_ACCOUNTING_ACCOUNT, money.ACCT_SKILL_TRAINING);
        setObjVar(player, PRECU_OP_ACCOUNTING_OUTCOME, PRECU_ACCOUNTING_OUTCOME_NONE);
        setObjVar(player, PRECU_OP_STATE, "accountingRequested");
        setObjVar(player, PRECU_OP_UPDATED, getCalendarTime());
        return attemptKey.equals(getStringObjVar(player, PRECU_OP_ACCOUNTING_ATTEMPT_KEY)) &&
            money.ACCT_SKILL_TRAINING.equals(
                getStringObjVar(player, PRECU_OP_ACCOUNTING_ACCOUNT)) &&
            PRECU_ACCOUNTING_OUTCOME_NONE.equals(
                getStringObjVar(player, PRECU_OP_ACCOUNTING_OUTCOME)) &&
            "accountingRequested".equals(getStringObjVar(player, PRECU_OP_STATE));
    }
    private boolean claimInitialPhaseARefund(
        obj_id trainer,
        obj_id player,
        dictionary params) throws InterruptedException
    {
        if (!hasExactSuccessfulPaymentProvenance(params) ||
            !isExactActivePhaseAOperation(
                trainer, player, params, "purchaseApplying", "", PRECU_VECTOR_DEBIT) ||
            getIntObjVar(player, PRECU_OP_PROTOCOL_VERSION) != PRECU_PROTOCOL_VERSION ||
            getIntObjVar(player, PRECU_OP_REFUND_GENERATION) != 0 ||
            !"none".equals(getStringObjVar(player, PRECU_OP_REFUND_ATTEMPT_KEY)) ||
            getIntObjVar(player, PRECU_OP_REFUND_RETRY_CONSUMED) != 0 ||
            !"none".equals(getStringObjVar(player, PRECU_OP_ACCOUNTING_ATTEMPT_KEY)) ||
            !"none".equals(getStringObjVar(player, PRECU_OP_ACCOUNTING_ACCOUNT)) ||
            !PRECU_ACCOUNTING_OUTCOME_NONE.equals(
                getStringObjVar(player, PRECU_OP_ACCOUNTING_OUTCOME)))
        {
            return false;
        }
        String attemptKey = buildPhaseAAttemptKey(
            params.getString(PRECU_PARAM_ID), "refund", 1);
        setObjVar(player, PRECU_OP_REFUND_GENERATION, 1);
        setObjVar(player, PRECU_OP_REFUND_ATTEMPT_KEY, attemptKey);
        setObjVar(player, PRECU_OP_REFUND_RETRY_CONSUMED, 0);
        setObjVar(player, PRECU_OP_STATE, "refundInitialClaiming");
        setObjVar(player, PRECU_OP_UPDATED, getCalendarTime());
        params.put(PRECU_REFUND_PARAM_GENERATION, 1);
        params.put(PRECU_REFUND_PARAM_ATTEMPT_KEY, attemptKey);
        params.put(PRECU_REFUND_PARAM_RETRY_CONSUMED, false);
        return hasExactPhaseARefundAttempt(
            trainer,
            player,
            params,
            "refundInitialClaiming",
            "",
            PRECU_VECTOR_DEBIT,
            1);
    }
    private boolean publishPhaseAAccountingRequestQueueFailure(
        obj_id trainer,
        obj_id player,
        dictionary params) throws InterruptedException
    {
        String expectedKey = buildPhaseAAttemptKey(
            params.getString(PRECU_PARAM_ID), "accounting", 1);
        if (!hasExactSuccessfulPaymentProvenance(params) ||
            !isExactActivePhaseAOperation(
                trainer, player, params, "accountingRequested", "", PRECU_VECTOR_HELD) ||
            !expectedKey.equals(
                params.getString(PRECU_ACCOUNTING_PARAM_ATTEMPT_KEY)) ||
            !expectedKey.equals(
                getStringObjVar(player, PRECU_OP_ACCOUNTING_ATTEMPT_KEY)) ||
            !money.ACCT_SKILL_TRAINING.equals(params.getString(money.DICT_ACCT_NAME)) ||
            !money.ACCT_SKILL_TRAINING.equals(
                getStringObjVar(player, PRECU_OP_ACCOUNTING_ACCOUNT)) ||
            !PRECU_ACCOUNTING_OUTCOME_NONE.equals(
                getStringObjVar(player, PRECU_OP_ACCOUNTING_OUTCOME)))
        {
            return false;
        }
        setObjVar(
            player,
            PRECU_OP_ACCOUNTING_OUTCOME,
            PRECU_ACCOUNTING_OUTCOME_REQUEST_QUEUE_FAILED);
        if (!PRECU_ACCOUNTING_OUTCOME_REQUEST_QUEUE_FAILED.equals(
                getStringObjVar(player, PRECU_OP_ACCOUNTING_OUTCOME)))
        {
            return false;
        }
        setObjVar(player, PRECU_OP_STATE, "accountingRequestQueueFailed");
        setObjVar(player, PRECU_OP_UPDATED, getCalendarTime());
        return "accountingRequestQueueFailed".equals(
            getStringObjVar(player, PRECU_OP_STATE));
    }
    private boolean dispatchClaimedPhaseARefund(
        obj_id trainer,
        obj_id player,
        dictionary params,
        int generation) throws InterruptedException
    {
        String family = generation == 1 ? "refundInitial" : "refundRecovery";
        if ((generation != 1 && generation != 2) ||
            !hasExactPhaseARefundAttempt(
                trainer,
                player,
                params,
                family + "Claiming",
                "",
                PRECU_VECTOR_DEBIT,
                generation))
        {
            return false;
        }
        setObjVar(player, PRECU_OP_STATE, family + "Dispatching");
        setObjVar(player, PRECU_OP_UPDATED, getCalendarTime());
        if (!hasExactPhaseARefundAttempt(
                trainer,
                player,
                params,
                family + "Dispatching",
                "",
                PRECU_VECTOR_DEBIT,
                generation))
        {
            return false;
        }
        boolean queued = transferBankCreditsTo(
            trainer,
            player,
            params.getInt(money.DICT_TOTAL),
            "precuPhaseARefundSucceeded",
            "precuPhaseARefundFailed",
            params);
        if (!queued)
        {
            transitionExactPhaseARefund(
                trainer,
                player,
                params,
                family + "Dispatching",
                "",
                family + "Failed",
                PRECU_VECTOR_DEBIT,
                generation);
        }
        else
        {
            checkpointPhaseARefundPending(trainer, player, params, generation);
        }
        return queued;
    }
    private boolean hasAnyPhaseATag(dictionary params)
    {
        return params != null && !params.isEmpty() &&
            (params.containsKey(PRECU_PARAM_ID) || params.containsKey(PRECU_PARAM_KIND) ||
                params.containsKey(PRECU_LIFECYCLE_PARAM_ID) ||
                params.containsKey(PRECU_PROTOCOL_PARAM_VERSION) ||
                params.containsKey(PRECU_REFUND_PARAM_GENERATION) ||
                params.containsKey(PRECU_REFUND_PARAM_ATTEMPT_KEY) ||
                params.containsKey(PRECU_REFUND_PARAM_RETRY_CONSUMED) ||
                params.containsKey(PRECU_ACCOUNTING_PARAM_ATTEMPT_KEY));
    }
    private boolean isExactActivePhaseAOperation(
        obj_id trainer,
        obj_id player,
        dictionary params,
        String expectedState,
        String alternateState,
        String expectedVector) throws InterruptedException
    {
        if (!isIdValid(trainer) || !trainer.isLoaded() || !trainer.isAuthoritative() ||
            !isIdValid(player) || !isPlayer(player) || !player.isLoaded() ||
            !player.isAuthoritative() || params == null || params.isEmpty() ||
            !hasObjVar(player, PRECU_OP_ATTEMPT_ID) || !hasObjVar(player, PRECU_OP_ID) ||
            !hasObjVar(player, PRECU_OP_KIND) || !hasObjVar(player, PRECU_OP_STATE) ||
            !hasObjVar(player, PRECU_OP_UPDATED) ||
            !hasObjVar(player, PRECU_OP_LIFECYCLE_ID) ||
            !hasObjVar(player, PRECU_OP_TRAINER_OID) ||
            !hasObjVar(player, PRECU_OP_SKILL_NAME) ||
            !hasObjVar(player, PRECU_OP_COST) ||
            !hasObjVar(player, PRECU_OP_PRE_CREDITS) ||
            !hasObjVar(player, PRECU_OP_PRE_CASH) ||
            !hasObjVar(player, PRECU_OP_PRE_BANK) ||
            !hasObjVar(player, PRECU_OP_PRE_XP) ||
            !hasObjVar(player, PRECU_OP_PRE_POINTS) ||
            !hasObjVar(player, PRECU_OP_PRE_CAP) ||
            !hasObjVar(player, PRECU_OP_PRE_NOVICE) ||
            !hasObjVar(player, PRECU_OP_PRE_SKILL) ||
            !hasObjVar(player, PRECU_OP_PROTOCOL_VERSION) ||
            !hasObjVar(player, PRECU_OP_REFUND_GENERATION) ||
            !hasObjVar(player, PRECU_OP_REFUND_ATTEMPT_KEY) ||
            !hasObjVar(player, PRECU_OP_REFUND_RETRY_CONSUMED) ||
            !hasObjVar(player, PRECU_OP_ACCOUNTING_ATTEMPT_KEY) ||
            !hasObjVar(player, PRECU_OP_ACCOUNTING_ACCOUNT) ||
            !hasObjVar(player, PRECU_OP_ACCOUNTING_OUTCOME) ||
            !hasObjVar(player, PRECU_LIFECYCLE_ATTEMPT_ID) ||
            !hasObjVar(player, PRECU_LIFECYCLE_ID) ||
            !hasObjVar(player, PRECU_LIFECYCLE_STATE) ||
            !hasObjVar(player, PRECU_LIFECYCLE_BASE_CASH) ||
            !hasObjVar(player, PRECU_LIFECYCLE_BASE_BANK) ||
            !hasObjVar(player, PRECU_LIFECYCLE_BASE_XP) ||
            !hasObjVar(player, PRECU_LIFECYCLE_BASE_POINTS) ||
            !hasObjVar(player, PRECU_LIFECYCLE_BASE_CAP) ||
            !hasObjVar(player, PRECU_LIFECYCLE_BASE_NOVICE) ||
            !hasObjVar(player, PRECU_LIFECYCLE_BASE_SKILL))
        {
            return false;
        }
        String operationId = params.getString(PRECU_PARAM_ID);
        String operationKind = params.getString(PRECU_PARAM_KIND);
        String lifecycleId = params.getString(PRECU_LIFECYCLE_PARAM_ID);
        String currentState = getStringObjVar(player, PRECU_OP_STATE);
        String skillName = params.getString("skillName");
        obj_id taggedPlayer = params.getObjId(money.DICT_PLAYER_ID);
        obj_id taggedTrainer = params.getObjId(money.DICT_TARGET_ID);
        int operationCost = getIntObjVar(player, PRECU_OP_COST);
        String operationSkillName = getStringObjVar(player, PRECU_OP_SKILL_NAME);
        if (!params.containsKey(PRECU_PARAM_ID) ||
            !params.containsKey(PRECU_PARAM_KIND) ||
            !params.containsKey(PRECU_LIFECYCLE_PARAM_ID) ||
            !params.containsKey(PRECU_PROTOCOL_PARAM_VERSION) ||
            !params.containsKey("skillName") ||
            !params.containsKey(money.DICT_PLAYER_ID) ||
            !params.containsKey(money.DICT_TARGET_ID) ||
            !params.containsKey(money.DICT_AMOUNT) ||
            !params.containsKey(money.DICT_TOTAL) ||
            operationId == null || !operationId.matches("[a-f0-9]{32}") ||
            !operationId.equals(getStringObjVar(player, PRECU_OP_ATTEMPT_ID)) ||
            !operationId.equals(getStringObjVar(player, PRECU_OP_ID)) ||
            !"purchase".equals(operationKind) ||
            !operationKind.equals(getStringObjVar(player, PRECU_OP_KIND)) ||
            lifecycleId == null || !lifecycleId.matches("[a-f0-9]{32}") ||
            !lifecycleId.equals(getStringObjVar(player, PRECU_OP_LIFECYCLE_ID)) ||
            !lifecycleId.equals(getStringObjVar(player, PRECU_LIFECYCLE_ATTEMPT_ID)) ||
            !lifecycleId.equals(getStringObjVar(player, PRECU_LIFECYCLE_ID)) ||
            !"established".equals(getStringObjVar(player, PRECU_LIFECYCLE_STATE)) ||
            taggedPlayer == null || !player.equals(taggedPlayer) ||
            taggedTrainer == null || !trainer.equals(taggedTrainer) ||
            !trainer.toString().equals(getStringObjVar(player, PRECU_OP_TRAINER_OID)) ||
            skillName == null || !PRECU_CRAFTING_SKILL.equals(skillName) ||
            !skillName.equals(operationSkillName) ||
            params.getInt(PRECU_PROTOCOL_PARAM_VERSION) != PRECU_PROTOCOL_VERSION ||
            getIntObjVar(player, PRECU_OP_PROTOCOL_VERSION) != PRECU_PROTOCOL_VERSION ||
            operationCost != PRECU_CRAFTING_TRAINER_COST ||
            getIntObjVar(player, PRECU_OP_UPDATED) <= 0 ||
            params.getInt(money.DICT_AMOUNT) != operationCost ||
            params.getInt(money.DICT_TOTAL) != operationCost ||
            !matchesPhaseAState(currentState, expectedState, alternateState))
        {
            return false;
        }

        int preCredits = getIntObjVar(player, PRECU_OP_PRE_CREDITS);
        int preCash = getIntObjVar(player, PRECU_OP_PRE_CASH);
        int preBank = getIntObjVar(player, PRECU_OP_PRE_BANK);
        int preXp = getIntObjVar(player, PRECU_OP_PRE_XP);
        int prePoints = getIntObjVar(player, PRECU_OP_PRE_POINTS);
        int preCap = getIntObjVar(player, PRECU_OP_PRE_CAP);
        int preNovice = getIntObjVar(player, PRECU_OP_PRE_NOVICE);
        int preSkill = getIntObjVar(player, PRECU_OP_PRE_SKILL);
        int baseCash = getIntObjVar(player, PRECU_LIFECYCLE_BASE_CASH);
        int baseBank = getIntObjVar(player, PRECU_LIFECYCLE_BASE_BANK);
        int baseXp = getIntObjVar(player, PRECU_LIFECYCLE_BASE_XP);
        int basePoints = getIntObjVar(player, PRECU_LIFECYCLE_BASE_POINTS);
        int baseCap = getIntObjVar(player, PRECU_LIFECYCLE_BASE_CAP);
        int baseNovice = getIntObjVar(player, PRECU_LIFECYCLE_BASE_NOVICE);
        int baseSkill = getIntObjVar(player, PRECU_LIFECYCLE_BASE_SKILL);
        int novicePointCost = skill.getSkillPointCost(PRECU_CRAFTING_NOVICE_SKILL);
        int targetPointCost = skill.getSkillPointCost(PRECU_CRAFTING_SKILL);
        int expectedPrePoints = basePoints - (baseNovice == 0 ? novicePointCost : 0);
        if (baseCash < 0 || baseBank < 0 || preCredits < operationCost ||
            preCash < 0 || preBank < operationCost ||
            (long)preCredits != (long)preCash + (long)preBank ||
            preCash != baseCash ||
            (long)preBank != (long)baseBank + (long)operationCost ||
            (long)preXp != (long)baseXp + (long)PRECU_CRAFTING_XP_COST ||
            novicePointCost < 0 || targetPointCost < 0 ||
            prePoints != expectedPrePoints || prePoints < targetPointCost ||
            preCap != PRECU_PREPURCHASE_XP_CAP ||
            (baseNovice == 1 && baseCap != preCap) ||
            (baseNovice != 0 && baseNovice != 1) || baseSkill != 0 ||
            preNovice != 1 || preSkill != 0)
        {
            return false;
        }

        int bankDebit = preBank < operationCost ? preBank : operationCost;
        int cashDebit = operationCost - bankDebit;
        boolean preGameplay =
            getExperiencePoints(player, PRECU_CRAFTING_XP_TYPE) == preXp &&
            skill.getAvailableSkillPoints(player) == prePoints &&
            getExperienceCap(player, PRECU_CRAFTING_XP_TYPE) == preCap &&
            (hasSkill(player, PRECU_CRAFTING_NOVICE_SKILL) ? 1 : 0) == preNovice &&
            (hasSkill(player, PRECU_CRAFTING_SKILL) ? 1 : 0) == preSkill;
        if (PRECU_VECTOR_PRE.equals(expectedVector) ||
            PRECU_VECTOR_REFUND.equals(expectedVector))
        {
            return preGameplay && !utils.hasScriptVar(player, PRECU_RELOG_NONCE) &&
                !utils.hasScriptVar(player, PRECU_RESTART_NONCE) &&
                hasExactPreparedPhaseACraftingVector(player) &&
                getCashBalance(player) == preCash &&
                getBankBalance(player) == preBank &&
                getTotalMoney(player) == preCredits;
        }
        if (PRECU_VECTOR_DEBIT.equals(expectedVector))
        {
            return preGameplay && !utils.hasScriptVar(player, PRECU_RELOG_NONCE) &&
                !utils.hasScriptVar(player, PRECU_RESTART_NONCE) &&
                hasExactPreparedPhaseACraftingVector(player) &&
                getCashBalance(player) == preCash - cashDebit &&
                getBankBalance(player) == preBank - bankDebit &&
                getTotalMoney(player) == preCredits - operationCost;
        }
        if (PRECU_VECTOR_HELD.equals(expectedVector))
        {
            return getCashBalance(player) == preCash - cashDebit &&
                getBankBalance(player) == preBank - bankDebit &&
                getTotalMoney(player) == preCredits - operationCost &&
                getExperiencePoints(player, PRECU_CRAFTING_XP_TYPE) ==
                    preXp - PRECU_CRAFTING_XP_COST &&
                skill.getAvailableSkillPoints(player) == prePoints - targetPointCost &&
                getExperienceCap(player, PRECU_CRAFTING_XP_TYPE) == PRECU_TRAINED_XP_CAP &&
                hasExactHeldPhaseACraftingVector(player) &&
                !utils.hasScriptVar(player, PRECU_RESTART_NONCE) &&
                (!utils.hasScriptVar(player, PRECU_RELOG_NONCE) ||
                    operationId.equals(
                        utils.getStringScriptVar(player, PRECU_RELOG_NONCE)));
        }
        return false;
    }
    private boolean containsPhaseAValue(String[] values, String expected)
    {
        if (values == null || expected == null)
        {
            return false;
        }
        for (String value : values)
        {
            if (expected.equals(value))
            {
                return true;
            }
        }
        return false;
    }
    private boolean hasExactPhaseASchematicVector(obj_id player, boolean held)
        throws InterruptedException
    {
        String[] groupIds = dataTableGetStringColumnNoDefaults(
            PRECU_SCHEMATIC_GROUP_TABLE,
            "GroupId");
        String[] schematicNames = dataTableGetStringColumnNoDefaults(
            PRECU_SCHEMATIC_GROUP_TABLE,
            "SchematicName");
        if (groupIds == null || schematicNames == null ||
            groupIds.length != schematicNames.length)
        {
            return false;
        }
        Vector<String> seen = new Vector<String>();
        for (int i = 0; i < groupIds.length; ++i)
        {
            if (!containsPhaseAValue(PRECU_CRAFTING_VECTOR_SCHEMATIC_GROUPS, groupIds[i]))
            {
                continue;
            }
            String schematicName = schematicNames[i];
            if (schematicName == null || schematicName.length() == 0 ||
                seen.contains(schematicName))
            {
                return false;
            }
            seen.add(schematicName);
            boolean shouldOwn = held ||
                !containsPhaseAValue(PRECU_CRAFTING_PURCHASE_SCHEMATICS, schematicName);
            if (hasSchematic(player, schematicName) != shouldOwn)
            {
                return false;
            }
        }
        return seen.size() == PRECU_CRAFTING_SCHEMATIC_COUNT;
    }
    private boolean hasExactPreparedPhaseACraftingVector(obj_id player)
        throws InterruptedException
    {
        if (!hasSkill(player, PRECU_CRAFTING_NOVICE_SKILL) ||
            hasSkill(player, PRECU_CRAFTING_SKILL) ||
            !hasCommand(player, PRECU_CRAFTING_VECTOR_COMMANDS[0]) ||
            !hasCommand(player, PRECU_CRAFTING_VECTOR_COMMANDS[1]) ||
            !hasCommand(player, PRECU_CRAFTING_VECTOR_COMMANDS[2]) ||
            hasCommand(player, PRECU_CRAFTING_VECTOR_COMMANDS[3]) ||
            (hasObjVar(player, "newbie.hasSkill") && !hasObjVar(player, "newbie.trained")))
        {
            return false;
        }
        for (int i = 0; i < PRECU_CRAFTING_VECTOR_MODS.length; ++i)
        {
            if (getSkillStatisticModifier(player, PRECU_CRAFTING_VECTOR_MODS[i]) !=
                PRECU_CRAFTING_VECTOR_MOD_VALUES[i] -
                    PRECU_CRAFTING_PURCHASE_MOD_DELTAS[i])
            {
                return false;
            }
        }
        return hasExactPhaseASchematicVector(player, false);
    }
    private boolean hasExactHeldPhaseACraftingVector(obj_id player)
        throws InterruptedException
    {
        if (!hasSkill(player, PRECU_CRAFTING_NOVICE_SKILL) ||
            !hasSkill(player, PRECU_CRAFTING_SKILL))
        {
            return false;
        }
        for (String command : PRECU_CRAFTING_VECTOR_COMMANDS)
        {
            if (!hasCommand(player, command))
            {
                return false;
            }
        }
        for (int i = 0; i < PRECU_CRAFTING_VECTOR_MODS.length; ++i)
        {
            if (getSkillStatisticModifier(player, PRECU_CRAFTING_VECTOR_MODS[i]) !=
                PRECU_CRAFTING_VECTOR_MOD_VALUES[i])
            {
                return false;
            }
        }
        return hasExactPhaseASchematicVector(player, true);
    }
    private boolean matchesPhaseAState(
        String currentState,
        String expectedState,
        String alternateState)
    {
        return expectedState.equals(currentState) ||
            (alternateState.length() > 0 && alternateState.equals(currentState));
    }
    private boolean transitionPhaseAOperation(
        obj_id trainer,
        obj_id player,
        dictionary params,
        String expectedState,
        String alternateState,
        String state,
        String expectedVector)
        throws InterruptedException
    {
        if (!isExactActivePhaseAOperation(
                trainer, player, params, expectedState, alternateState, expectedVector))
        {
            return false;
        }
        setObjVar(player, PRECU_OP_STATE, state);
        setObjVar(player, PRECU_OP_UPDATED, getCalendarTime());
        return state.equals(getStringObjVar(player, PRECU_OP_STATE));
    }
    private boolean hasExactPhaseARefundAttempt(
        obj_id trainer,
        obj_id player,
        dictionary params,
        String expectedState,
        String alternateState,
        String expectedVector,
        int generation) throws InterruptedException
    {
        if (!hasExactSuccessfulPaymentProvenance(params) ||
            !params.containsKey(PRECU_REFUND_PARAM_GENERATION) ||
            !params.containsKey(PRECU_REFUND_PARAM_ATTEMPT_KEY) ||
            !params.containsKey(PRECU_REFUND_PARAM_RETRY_CONSUMED) ||
            !isExactActivePhaseAOperation(
                trainer,
                player,
                params,
                expectedState,
                alternateState,
                expectedVector))
        {
            return false;
        }
        String expectedKey = buildPhaseAAttemptKey(
            params.getString(PRECU_PARAM_ID), "refund", generation);
        boolean consumed = generation == 2;
        return (generation == 1 || generation == 2) &&
            getIntObjVar(player, PRECU_OP_REFUND_GENERATION) == generation &&
            expectedKey.equals(getStringObjVar(player, PRECU_OP_REFUND_ATTEMPT_KEY)) &&
            params.getInt(PRECU_REFUND_PARAM_GENERATION) == generation &&
            expectedKey.equals(params.getString(PRECU_REFUND_PARAM_ATTEMPT_KEY)) &&
            (getIntObjVar(player, PRECU_OP_REFUND_RETRY_CONSUMED) != 0) == consumed &&
            params.getBoolean(PRECU_REFUND_PARAM_RETRY_CONSUMED) == consumed &&
            !"none".equals(expectedKey) &&
            "none".equals(getStringObjVar(player, PRECU_OP_ACCOUNTING_ATTEMPT_KEY)) &&
            "none".equals(getStringObjVar(player, PRECU_OP_ACCOUNTING_ACCOUNT)) &&
            PRECU_ACCOUNTING_OUTCOME_NONE.equals(
                getStringObjVar(player, PRECU_OP_ACCOUNTING_OUTCOME));
    }
    private boolean transitionExactPhaseARefund(
        obj_id trainer,
        obj_id player,
        dictionary params,
        String expectedState,
        String alternateState,
        String state,
        String expectedVector,
        int generation) throws InterruptedException
    {
        if (!hasExactPhaseARefundAttempt(
                trainer,
                player,
                params,
                expectedState,
                alternateState,
                expectedVector,
                generation))
        {
            return false;
        }
        setObjVar(player, PRECU_OP_STATE, state);
        setObjVar(player, PRECU_OP_UPDATED, getCalendarTime());
        return state.equals(getStringObjVar(player, PRECU_OP_STATE));
    }
    private boolean checkpointPhaseARefundPending(
        obj_id trainer,
        obj_id player,
        dictionary params,
        int generation) throws InterruptedException
    {
        String family = generation == 1 ? "refundInitial" : "refundRecovery";
        boolean exactTransit = hasExactPhaseARefundAttempt(
            trainer,
            player,
            params,
            family + "Dispatching",
            "",
            PRECU_VECTOR_DEBIT,
            generation);
        if (!exactTransit)
        {
            exactTransit = hasExactPhaseARefundAttempt(
                trainer,
                player,
                params,
                family + "Dispatching",
                "",
                PRECU_VECTOR_REFUND,
                generation);
        }
        if (!exactTransit)
        {
            return false;
        }
        setObjVar(player, PRECU_OP_STATE, family + "Pending");
        setObjVar(player, PRECU_OP_UPDATED, getCalendarTime());
        return (family + "Pending").equals(getStringObjVar(player, PRECU_OP_STATE));
    }
    public boolean completeSkillPurchase(obj_id player, String skillName) throws InterruptedException
    {
        if (!isIdValid(player) || (!isPlayer(player)))
        {
            return false;
        }
        if ((skillName == null) || (skillName.equals("")))
        {
            return false;
        }
        boolean learned = true;
        prose_package pp;
        if (skill.purchaseSkill(player, skillName))
        {
            pp = prose.getPackage(PROSE_SKILL_LEARNED, new string_id(SKILL_N, skillName));
            if (fs_quests.isVillageEligible(player))
            {
                if (!hasObjVar(player, fs_quests.VAR_VILLAGE_COMPLETE))
                {
                    if (skillName.contains("force_sensitive_"))
                    {
                        if (fs_quests.getBranchesLearned(player) >= 6)
                        {
                            setObjVar(player, fs_quests.VAR_VILLAGE_COMPLETE, 1);
                            CustomerServiceLog("fs_quests", "%TU has completed the village by attaining six FS skill branches.", player, null);
                        }
                    }
                }
            }
        }
        else 
        {
            pp = prose.getPackage(PROSE_TRAIN_FAILED, new string_id(SKILL_N, skillName));
            learned = false;
        }
        sendSystemMessageProse(player, pp);
        return learned;
    }
    public String[] getSkillData(String skillName, obj_id player) throws InterruptedException
    {
        if (skillName.equals(""))
        {
            return null;
        }
        Vector ret = new Vector();
        ret.setSize(0);
        ret = utils.addElement(ret, "REQUIRED SKILLS");
        String[] skillReqs = getSkillPrerequisiteSkills(skillName);
        if (skillReqs == null)
        {
            ret = utils.addElement(ret, " none");
        }
        else 
        {
            for (String skillReq : skillReqs) {
                String sName = getString(new string_id("skl_n", skillReq));
                ret = utils.addElement(ret, " " + sName);
            }
        }
        ret = utils.addElement(ret, "XP COSTS");
        dictionary xpReqs = getSkillPrerequisiteExperience(skillName);
        if ((xpReqs == null) || (xpReqs.isEmpty()))
        {
            ret = utils.addElement(ret, " none");
        }
        else 
        {
            java.util.Enumeration xp = xpReqs.keys();
            while (xp.hasMoreElements())
            {
                String xpType = (String)xp.nextElement();
                String sXp = getString(new string_id("exp_n", xpType));
                ret = utils.addElement(ret, " " + sXp + " = " + xpReqs.getInt(xpType));
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
    public boolean checkSkillStatus(obj_id trainer, obj_id player) throws InterruptedException
    {
        if (!isIdValid(trainer) || !isIdValid(player))
        {
            return false;
        }
        if (isJedi(player))
        {
            if (jedi.isJediTrainerForPlayer(player, trainer))
            {
                return true;
            }
        }
        obj_id self = trainer;
        obj_id speaker = player;
        String[] pSkills = getSkillListingForPlayer(speaker);
        String[] tSkills = skill.getTeacherSkills(trainer, speaker);
        if (tSkills == null || tSkills.length == 0)
        {
            return false;
        }
        String convo = CONVOFILE;
        if (jedi.isJediTrainerForPlayer(player, trainer))
        {
            convo = JEDI_TRAINER;
        }
        String[] lowSkills = getSkillPrerequisiteSkills(tSkills[0]);
        if (lowSkills != null && lowSkills.length > 0)
        {
            if (!utils.isSubset(pSkills, lowSkills))
            {
                string_id msg = new string_id(convo, "no_qualify");
                chat.chat(self, speaker, msg, chat.ChatFlag_targetOnly);
                npcEndConversation(speaker);
                Vector entries = new Vector();
                entries.setSize(0);
                for (String lowSkill : lowSkills) {
                    entries = utils.addElement(entries, "@skl_n:" + lowSkill);
                }
                if (entries != null && entries.size() > 0)
                {
                    String title = "@skill_teacher:no_qualify_title";
                    String prompt = "@skill_teacher:no_qualify_prompt";
                    sui.listbox(self, player, prompt, sui.OK_ONLY, title, entries, "noHandler");
                }
                return false;
            }
        }
        if (utils.isSubset(pSkills, tSkills))
        {
            string_id msg = new string_id(convo, "topped_out");
            chat.chat(self, speaker, msg, chat.ChatFlag_targetOnly);
            npcEndConversation(speaker);
            return false;
        }
        if (skill.getAvailableSkillPoints(speaker) <= 0)
        {
            string_id msg = new string_id(convo, "no_skill_pts");
            chat.chat(self, speaker, msg, chat.ChatFlag_targetOnly);
            npcEndConversation(speaker);
            return false;
        }
        return true;
    }
    public boolean hasSurpassedTrainer(obj_id trainer, obj_id player) throws InterruptedException
    {
        String[] pSkills = getSkillListingForPlayer(player);
        String[] tSkills = skill.getTeacherSkills(trainer, player);
        if (tSkills == null || tSkills.length == 0)
        {
            return false;
        }
        return utils.isSubset(pSkills, tSkills);
    }
    public int OnTriggerVolumeEntered(obj_id self, String volumeName, obj_id breacher) throws InterruptedException
    {
        if (!isPlayer(breacher))
        {
            return SCRIPT_CONTINUE;
        }
        if (!volumeName.equals(FACETO_VOLUME_NAME))
        {
            return SCRIPT_CONTINUE;
        }
        if (isInNpcConversation(self))
        {
            return SCRIPT_CONTINUE;
        }
        if (canSee(self, breacher))
        {
            faceTo(self, breacher);
        }
        return SCRIPT_CONTINUE;
    }
    public String getJediConvoFile(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !isJedi(player))
        {
            return null;
        }
        if (hasSkill(player, "jedi_light_side_journeyman_novice"))
        {
            return JEDI_TRAINER_LIGHT;
        }
        if (hasSkill(player, "jedi_dark_side_journeyman_novice"))
        {
            return JEDI_TRAINER_DARK;
        }
        return JEDI_TRAINER;
    }
}
