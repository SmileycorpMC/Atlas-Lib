package net.smileycorp.atlas.api.config;

import com.google.common.base.Optional;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConfigurableHolder<T> {
    
    private final Supplier<T> supplier;
    private final Supplier<Boolean> shouldEnable;
    private T object;
    
    public ConfigurableHolder(Supplier<T> supplier, Supplier<Boolean> shouldEnable) {
        this.supplier = supplier;
        this.shouldEnable = shouldEnable;
    }
    
    public boolean isEnabled() {
        return shouldEnable.get();
    }
    
    public T get() {
        this.object = supplier.get();
        return object;
    }

    public void applyIfEnabled(Consumer<T> consumer) {
        if (isEnabled()) consumer.accept(get());
    }

    public <U> Optional<U> getIfEnabled(Function<T, U> function) {
        return isEnabled() ? Optional.of(function.apply(get())) : Optional.absent();
    }
    
}
