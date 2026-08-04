package script.conversation;

import script.library.ai_lib;
import script.library.chat;
import script.library.storyteller;
import script.library.utils;
import script.*;

public class storyteller_vendor extends script.base_script
{
    public storyteller_vendor()
    {
    }
    public static String c_stringFile = "conversation/storyteller_vendor";
    public boolean storyteller_vendor_condition__defaultCondition(obj_id player, obj_id npc) throws InterruptedException
    {
        return true;
    }
    public boolean storyteller_vendor_condition_checkFromToken(obj_id player, obj_id npc) throws InterruptedException
    {
        if (isFreeTrialAccount(player))
        {
            return false;
        }
        if (hasObjVar(npc, "storytellerid"))
        {
            obj_id storytelledId = getObjIdObjVar(npc, "storytellerid");
            if (isIdValid(storytelledId))
            {
                if (storytelledId == player || storytelledId == utils.getObjIdScriptVar(player, "storytellerAssistant"))
                {
                    return true;
                }
                else 
                {
                    return false;
                }
            }
            else 
            {
                return false;
            }
        }
        return true;
    }
    public void storyteller_vendor_action_showStorytellerVendorSui(obj_id player, obj_id npc) throws InterruptedException
    {
        storyteller.displayAvailableStorytellerTokenTypes(player, npc);
        return;
    }
    public int storyteller_vendor_handleBranch1(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        if (response.equals("s_6"))
        {
            if (storyteller_vendor_condition__defaultCondition(player, npc))
            {
                storyteller_vendor_action_showStorytellerVendorSui(player, npc);
                string_id message = new string_id(c_stringFile, "s_8");
                utils.removeScriptVar(player, "conversation.storyteller_vendor.branchId");
                npcEndConversationWithMessage(player, message);
                return SCRIPT_CONTINUE;
            }
        }
        if (response.equals("s_10"))
        {
            if (storyteller_vendor_condition__defaultCondition(player, npc))
            {
                string_id message = new string_id(c_stringFile, "s_12");
                utils.removeScriptVar(player, "conversation.storyteller_vendor.branchId");
                npcEndConversationWithMessage(player, message);
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_DEFAULT;
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        clearCondition(self, CONDITION_CONVERSABLE);
        detachScript(self, "conversation.storyteller_vendor");
        return SCRIPT_CONTINUE;
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        clearCondition(self, CONDITION_CONVERSABLE);
        detachScript(self, "conversation.storyteller_vendor");
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info menuInfo) throws InterruptedException
    {
        clearCondition(self, CONDITION_CONVERSABLE);
        return SCRIPT_CONTINUE;
    }
    public int OnIncapacitated(obj_id self, obj_id killer) throws InterruptedException
    {
        clearCondition(self, CONDITION_CONVERSABLE);
        detachScript(self, "conversation.storyteller_vendor");
        return SCRIPT_CONTINUE;
    }
    public boolean npcStartConversation(obj_id player, obj_id npc, String convoName, string_id greetingId, prose_package greetingProse, string_id[] responses) throws InterruptedException
    {
        Object[] objects = new Object[responses.length];
        System.arraycopy(responses, 0, objects, 0, responses.length);
        return npcStartConversation(player, npc, convoName, greetingId, greetingProse, objects);
    }
    public int OnStartNpcConversation(obj_id self, obj_id player) throws InterruptedException
    {
        return SCRIPT_OVERRIDE;
    }
    public int OnNpcConversationResponse(obj_id self, String conversationId, obj_id player, string_id response) throws InterruptedException
    {
        utils.removeScriptVar(player, "conversation.storyteller_vendor.branchId");
        return SCRIPT_CONTINUE;
    }
}
