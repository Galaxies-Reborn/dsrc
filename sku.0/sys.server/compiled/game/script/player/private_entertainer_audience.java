package script.player;

import script.*;
import script.library.*;

/**
 * Runs private watch/listen setup in player context so native trigger-volume
 * and entertained-heartbeat state is owned by the audience member.
 */
public class private_entertainer_audience extends script.base_script
{
    public int handlePrivateEntertainerWatch(
        obj_id self,
        dictionary params) throws InterruptedException
    {
        obj_id performer = getPerformer(params);
        if (!private_entertainer.activateWatching(self, performer))
        {
            sendSystemMessage(
                self,
                new string_id(
                    private_entertainer.STF,
                    "not_your_performer"));
            detachIfIdle(self);
        }
        return SCRIPT_CONTINUE;
    }

    public int handlePrivateEntertainerListen(
        obj_id self,
        dictionary params) throws InterruptedException
    {
        obj_id performer = getPerformer(params);
        if (!private_entertainer.activateListening(self, performer))
        {
            sendSystemMessage(
                self,
                new string_id(
                    private_entertainer.STF,
                    "not_your_performer"));
            detachIfIdle(self);
        }
        return SCRIPT_CONTINUE;
    }

    public int handlePrivateEntertainerAudienceCleanup(
        obj_id self,
        dictionary params) throws InterruptedException
    {
        private_entertainer.cleanupAudienceNow(
            self, getPerformer(params));
        detachIfIdle(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        obj_id activeDancer = hasObjVar(
            self, private_entertainer.VAR_ACTIVE_DANCER) ?
            getObjIdObjVar(
                self, private_entertainer.VAR_ACTIVE_DANCER) :
            obj_id.NULL_ID;
        obj_id activeMusician = hasObjVar(
            self, private_entertainer.VAR_ACTIVE_MUSICIAN) ?
            getObjIdObjVar(
                self, private_entertainer.VAR_ACTIVE_MUSICIAN) :
            obj_id.NULL_ID;
        obj_id resolvedDancer = private_entertainer.getActive(
            self, private_entertainer.TYPE_DANCER);
        obj_id resolvedMusician = private_entertainer.getActive(
            self, private_entertainer.TYPE_MUSICIAN);
        obj_id watchTarget = getPerformanceWatchTarget(self);
        if (isIdValid(activeDancer) && watchTarget == activeDancer &&
            (resolvedDancer != activeDancer ||
                !private_entertainer.canPlayerUse(
                    self,
                    watchTarget,
                    private_entertainer.TYPE_DANCER)))
        {
            private_entertainer.cleanupAudienceNow(self, watchTarget);
        }
        obj_id listenTarget = getPerformanceListenTarget(self);
        if (isIdValid(activeMusician) &&
            listenTarget == activeMusician &&
            (resolvedMusician != activeMusician ||
                !private_entertainer.canPlayerUse(
                    self,
                    listenTarget,
                    private_entertainer.TYPE_MUSICIAN)))
        {
            private_entertainer.cleanupAudienceNow(self, listenTarget);
        }
        detachIfIdle(self);
        return SCRIPT_CONTINUE;
    }

    private obj_id getPerformer(dictionary params)
    {
        if (params == null || params.isEmpty() ||
            !params.containsKey("performer"))
        {
            return obj_id.NULL_ID;
        }
        return params.getObjId("performer");
    }

    private void detachIfIdle(obj_id self) throws InterruptedException
    {
        if (!private_entertainer.hasPrivateAudienceState(self) &&
            hasScript(self, private_entertainer.SCRIPT_AUDIENCE))
        {
            detachScript(self, private_entertainer.SCRIPT_AUDIENCE);
        }
    }
}
