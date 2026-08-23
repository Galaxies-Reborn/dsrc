package script.item.container;

import script.*;
import script.library.account_containers;
import script.library.prose;
import script.library.utils;

public class account_bound extends script.base_script
{
    public account_bound()
    {
    }

    public static final string_id PROSE_WRONG_ITEM_TYPE = new string_id("error_message", "wrong_item_type");

    public int OnAttach(obj_id self) throws InterruptedException
    {
        account_containers.initializeContainer(self);
        bindToContainingPlayer(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        account_containers.initializeContainer(self);
        bindToContainingPlayer(self);
        return SCRIPT_CONTINUE;
    }

    public int OnAboutToBeTransferred(obj_id self, obj_id destContainer, obj_id transferer) throws InterruptedException
    {
        if (!account_containers.isManagedContainer(self) || !isIdValid(destContainer))
        {
            return SCRIPT_OVERRIDE;
        }

        obj_id destinationPlayer = getContainedBy(destContainer);
        if (!isIdValid(destinationPlayer) || !isPlayer(destinationPlayer) || utils.getPlayerDatapad(destinationPlayer) != destContainer)
        {
            return SCRIPT_OVERRIDE;
        }

        if (hasObjVar(self, account_containers.VAR_STATION_ID) && !account_containers.isBoundToPlayer(self, destinationPlayer))
        {
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }

    public int OnTransferred(obj_id self, obj_id sourceContainer, obj_id destContainer, obj_id transferer) throws InterruptedException
    {
        account_containers.initializeContainer(self);
        bindToContainingPlayer(self);
        return SCRIPT_CONTINUE;
    }

    public int OnAboutToReceiveItem(obj_id self, obj_id srcContainer, obj_id transferer, obj_id item) throws InterruptedException
    {
        if (!account_containers.isBoundToContainingAccount(self))
        {
            return SCRIPT_OVERRIDE;
        }
        if (account_containers.mayHoldItem(self, item))
        {
            return SCRIPT_CONTINUE;
        }

        if (isIdValid(transferer) && isPlayer(transferer))
        {
            string_id gotSid = getGameObjectTypeStringId(getGameObjectType(item));
            prose_package pp = prose.getPackage(PROSE_WRONG_ITEM_TYPE, self, gotSid);
            sendSystemMessageProse(transferer, pp);
        }
        return SCRIPT_OVERRIDE;
    }

    private void bindToContainingPlayer(obj_id self) throws InterruptedException
    {
        obj_id player = utils.getContainingPlayer(self);
        if (isIdValid(player) && isPlayer(player))
        {
            account_containers.bindContainer(self, player);
        }
    }
}
