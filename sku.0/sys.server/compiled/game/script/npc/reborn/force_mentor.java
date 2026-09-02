package script.npc.reborn;

import script.*;
import script.library.force_progression;

public class force_mentor extends script.base_script
{
    public static final String SCRIPT_NAME = "npc.reborn.force_mentor";
    public static final String CONVERSATION = "reborn_force_mentor";

    public int OnAttach(obj_id self) throws InterruptedException
    {
        reconcile(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        reconcile(self);
        return SCRIPT_CONTINUE;
    }

    public int reconcileForceMentor(obj_id self, dictionary params) throws InterruptedException
    {
        reconcile(self);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info menuInfo) throws InterruptedException
    {
        if (!force_progression.isReplacementEnabled() || !isIdValid(player) || !hasObjVar(self, force_progression.VAR_NPC_QUEST_ID))
        {
            return SCRIPT_CONTINUE;
        }
        int menu = menuInfo.addRootMenu(menu_info_types.CONVERSE_START, null);
        menu_info_data menuData = menuInfo.getMenuItemById(menu);
        menuData.setServerNotify(false);
        setCondition(self, CONDITION_CONVERSABLE);
        return SCRIPT_CONTINUE;
    }

    public int OnStartNpcConversation(obj_id self, obj_id player) throws InterruptedException
    {
        if (!force_progression.isReplacementEnabled() || !isIdValid(player) || !isPlayer(player))
        {
            return SCRIPT_OVERRIDE;
        }
        String questId = getQuestId(self);
        dictionary row = force_progression.getQuestRow(questId);
        if (row == null)
        {
            npcSpeak(player, new string_id(force_progression.STF, "unavailable"));
            npcEndConversation(player);
            return SCRIPT_CONTINUE;
        }
        if (force_progression.isQuestChainCompleted(player, questId))
        {
            showCompletedConversation(self, player, questId, row);
            return SCRIPT_CONTINUE;
        }
        int stage = force_progression.getQuestStage(player, questId);
        if (stage == 0)
        {
            if (!force_progression.canStartQuestChain(player, questId))
            {
                npcStartConversation(player, self, CONVERSATION, new string_id(force_progression.STF, "convergence_locked"), new string_id[] { new string_id(force_progression.STF, "leave") });
                return SCRIPT_CONTINUE;
            }
            npcStartConversation(player, self, CONVERSATION, force_progression.getQuestText(questId, "cue"), new string_id[]
            {
                new string_id(force_progression.STF, "accept"),
                new string_id(force_progression.STF, "leave")
            });
            return SCRIPT_CONTINUE;
        }
        String route = row.getString("route_family").toLowerCase();
        string_id prompt = new string_id(force_progression.STF, "route_" + route + "_step_" + stage);
        string_id[] choices = new string_id[3];
        for (int choice = 0; choice < choices.length; ++choice)
        {
            choices[choice] = new string_id(force_progression.STF, "route_" + route + "_step_" + stage + "_choice_" + choice);
        }
        npcStartConversation(player, self, CONVERSATION, prompt, choices);
        return SCRIPT_CONTINUE;
    }

    public int OnNpcConversationResponse(obj_id self, String conversation, obj_id player, string_id responseId) throws InterruptedException
    {
        if (!CONVERSATION.equals(conversation) || !force_progression.isReplacementEnabled() || !isIdValid(player))
        {
            return SCRIPT_CONTINUE;
        }
        String questId = getQuestId(self);
        String response = responseId.getAsciiId();
        if ("leave".equals(response))
        {
            npcEndConversation(player);
            return SCRIPT_CONTINUE;
        }
        if ("accept".equals(response))
        {
            boolean started = force_progression.beginQuestChain(player, questId);
            npcSpeak(player, new string_id(force_progression.STF, started ? "quest_started" : "unavailable"));
            npcEndConversation(player);
            return SCRIPT_CONTINUE;
        }
        if ("teach".equals(response))
        {
            if (!force_progression.isQuestChainCompleted(player, questId))
            {
                npcSpeak(player, new string_id(force_progression.STF, "unavailable"));
                npcEndConversation(player);
                return SCRIPT_CONTINUE;
            }
            String branch = force_progression.getQuestBranch(questId);
            boolean learned = force_progression.purchaseNextSkillInBranch(player, branch);
            npcSpeak(player, new string_id(force_progression.STF, learned ? "mentor_taught" : "mentor_cannot_teach"));
            npcEndConversation(player);
            return SCRIPT_CONTINUE;
        }
        int marker = response.lastIndexOf("_choice_");
        if (marker > -1 && response.length() == marker + 9)
        {
            int choice = response.charAt(response.length() - 1) - '0';
            int result = force_progression.answerQuestChain(player, questId, choice);
            String resultKey = "unavailable";
            if (result == force_progression.QUEST_RESULT_WAIT) resultKey = "quest_wait";
            else if (result == force_progression.QUEST_RESULT_ADVANCED) resultKey = "quest_advanced";
            else if (result == force_progression.QUEST_RESULT_WRONG) resultKey = "quest_wrong";
            else if (result == force_progression.QUEST_RESULT_COMPLETED) resultKey = "quest_completed";
            npcSpeak(player, new string_id(force_progression.STF, resultKey));
            npcEndConversation(player);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnDestroy(obj_id self) throws InterruptedException
    {
        if (hasObjVar(self, force_progression.VAR_NPC_OWNER))
        {
            obj_id owner = getObjIdObjVar(self, force_progression.VAR_NPC_OWNER);
            if (isIdValid(owner))
            {
                messageTo(owner, "reconcileForceProgressionNpcNetwork", null, 60.0f, false);
            }
        }
        return SCRIPT_CONTINUE;
    }

    private void reconcile(obj_id self) throws InterruptedException
    {
        if (!force_progression.isReplacementEnabled() || !hasObjVar(self, force_progression.VAR_NPC_QUEST_ID))
        {
            clearCondition(self, CONDITION_CONVERSABLE);
            return;
        }
        String questId = getQuestId(self);
        if (force_progression.getQuestRow(questId) == null)
        {
            clearCondition(self, CONDITION_CONVERSABLE);
            return;
        }
        setInvulnerable(self, true);
        setCondition(self, CONDITION_CONVERSABLE);
        setName(self, new string_id(force_progression.STF, "npc_" + questId));
    }

    private void showCompletedConversation(obj_id self, obj_id player, String questId, dictionary row) throws InterruptedException
    {
        String branch = row.getString("branch");
        String nextSkill = force_progression.getNextSkillInBranch(player, branch);
        if (getJediState(player) >= JEDI_STATE_FORCE_SENSITIVE && nextSkill != null)
        {
            npcStartConversation(player, self, CONVERSATION, new string_id(force_progression.STF, "mentor_ready"), new string_id[]
            {
                new string_id(force_progression.STF, "teach"),
                new string_id(force_progression.STF, "leave")
            });
            return;
        }
        string_id message = new string_id(force_progression.STF, getJediState(player) >= JEDI_STATE_FORCE_SENSITIVE ? "mentor_branch_complete" : "mentor_path_complete");
        npcStartConversation(player, self, CONVERSATION, message, new string_id[] { new string_id(force_progression.STF, "leave") });
    }

    private String getQuestId(obj_id self) throws InterruptedException
    {
        return hasObjVar(self, force_progression.VAR_NPC_QUEST_ID) ? getStringObjVar(self, force_progression.VAR_NPC_QUEST_ID) : "";
    }
}
