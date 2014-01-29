package oauthexample;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public final class Persist {
    private static final PersistenceManagerFactory pmfInstance =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private Persist() {}

    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}
