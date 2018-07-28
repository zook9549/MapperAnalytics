package team.refer.mapper.referral;

import team.refer.mapper.application.Application;

import java.io.Serializable;
import java.util.Objects;

public class ContextKey implements Serializable {
    public ContextKey() {
    }

    public ContextKey(Application application, long mappedMilliseconds) {
        this.application = application;
        this.mappedMilliseconds = mappedMilliseconds;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public long getMappedMilliseconds() {
        return mappedMilliseconds;
    }

    public void setMappedMilliseconds(long mappedMilliseconds) {
        this.mappedMilliseconds = mappedMilliseconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContextKey that = (ContextKey) o;
        return mappedMilliseconds == that.mappedMilliseconds &&
                Objects.equals(application, that.application);
    }

    @Override
    public int hashCode() {

        return Objects.hash(application, mappedMilliseconds);
    }

    private Application application;
    private long mappedMilliseconds;
}
