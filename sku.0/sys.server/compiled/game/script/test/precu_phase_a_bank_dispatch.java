package script.test;

import script.dictionary;
import script.library.money;
import script.obj_id;

/** One-shot owner-context bridge for fixture-bound Phase-A bank dispatch. */
public class precu_phase_a_bank_dispatch extends script.base_script
{
    private static final String SCRIPT_NAME = "test.precu_phase_a_bank_dispatch";
    private static final String OP_ROOT = "precu.phaseA.operation";
    private static final String OP_ATTEMPT_ID = OP_ROOT + ".attemptId";
    private static final String OP_ID = OP_ROOT + ".id";
    private static final String OP_KIND = OP_ROOT + ".kind";
    private static final String OP_STATE = OP_ROOT + ".state";
    private static final String OP_COST = OP_ROOT + ".cost";
    private static final String OP_LIFECYCLE_ID = OP_ROOT + ".lifecycleId";
    private static final String OP_PROTOCOL_VERSION = OP_ROOT + ".protocolVersion";
    private static final String PARAM_ID = "precuPhaseAOperationId";
    private static final String PARAM_KIND = "precuPhaseAOperationKind";
    private static final String PARAM_LIFECYCLE_ID = "precuPhaseALifecycleId";
    private static final String PARAM_PROTOCOL_VERSION = "precuPhaseAProtocolVersion";
    private static final String LOCATION_DISPATCH = "precuPhaseABankDispatch";
    private static final int PROTOCOL_VERSION = 64;

    public String executeDispatch(String params) throws InterruptedException
    {
        String[] args = params == null ? new String[0] : params.trim().split("\\s+");
        if (args.length != 2)
        {
            return "error=usage";
        }
        obj_id player;
        try
        {
            player = obj_id.getObjId(Long.parseLong(args[0]));
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidPlayer";
        }
        if (!isIdValid(player) || !isPlayer(player) || !player.isLoaded() ||
            !player.isAuthoritative() || !hasObjVar(player, OP_LIFECYCLE_ID) ||
            !args[1].equals(getStringObjVar(player, OP_LIFECYCLE_ID)) ||
            !hasObjVar(player, OP_STATE) ||
            !"queued".equals(getStringObjVar(player, OP_STATE)))
        {
            return "error=dispatchOwnership";
        }
        if (hasScript(player, SCRIPT_NAME))
        {
            detachScript(player, SCRIPT_NAME);
            return "action=detachBankDispatch queued=false";
        }
        attachScript(player, SCRIPT_NAME);
        return "action=attachBankDispatch queued=true";
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        return dispatchQueuedBankOperation(self);
    }

    public int OnLogin(obj_id self) throws InterruptedException
    {
        return dispatchQueuedBankOperation(self);
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        return dispatchQueuedBankOperation(self);
    }

    public int OnArrivedAtLocation(obj_id self, String name) throws InterruptedException
    {
        if (!LOCATION_DISPATCH.equals(name))
        {
            return SCRIPT_CONTINUE;
        }
        return dispatchQueuedBankOperation(self);
    }

    private int dispatchQueuedBankOperation(obj_id self) throws InterruptedException
    {
        try
        {
            if (!isIdValid(self) || !isPlayer(self) || !self.isLoaded() ||
                !self.isAuthoritative() ||
                !hasObjVar(self, OP_ATTEMPT_ID) || !hasObjVar(self, OP_ID) ||
                !hasObjVar(self, OP_KIND) || !hasObjVar(self, OP_STATE) ||
                !hasObjVar(self, OP_COST) || !hasObjVar(self, OP_LIFECYCLE_ID) ||
                !hasObjVar(self, OP_PROTOCOL_VERSION))
            {
                return SCRIPT_CONTINUE;
            }
            String operationId = getStringObjVar(self, OP_ID);
            String kind = getStringObjVar(self, OP_KIND);
            if (!operationId.equals(getStringObjVar(self, OP_ATTEMPT_ID)) ||
                (!"fund".equals(kind) && !"drain".equals(kind)) ||
                !"queued".equals(getStringObjVar(self, OP_STATE)) ||
                getIntObjVar(self, OP_PROTOCOL_VERSION) != PROTOCOL_VERSION)
            {
                return SCRIPT_CONTINUE;
            }
            dictionary params = new dictionary();
            params.put(PARAM_ID, operationId);
            params.put(PARAM_KIND, kind);
            params.put(PARAM_LIFECYCLE_ID, getStringObjVar(self, OP_LIFECYCLE_ID));
            params.put(PARAM_PROTOCOL_VERSION, PROTOCOL_VERSION);
            params.put(money.DICT_TOTAL, getIntObjVar(self, OP_COST));
            messageTo(self, "precuPhaseADispatchBankTransfer", params, 0, true);
        }
        finally
        {
        }
        return SCRIPT_CONTINUE;
    }
}
