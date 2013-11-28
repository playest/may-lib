package fr.irit.smac.may.lib.components.scheduling;

import fr.irit.smac.may.lib.components.scheduling.interfaces.AdvancedExecutor;
import fr.irit.smac.may.lib.components.scheduling.interfaces.SchedulingControl;
import fr.irit.smac.may.lib.interfaces.Do;

@SuppressWarnings("all")
public abstract class Scheduler {
  @SuppressWarnings("all")
  public interface Requires {
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public AdvancedExecutor executor();
  }
  
  
  @SuppressWarnings("all")
  public interface Provides {
    /**
     * This can be called to access the provided port.
     * 
     */
    public Do tick();
    
    /**
     * This can be called to access the provided port.
     * 
     */
    public SchedulingControl async();
  }
  
  
  @SuppressWarnings("all")
  public interface Component extends fr.irit.smac.may.lib.components.scheduling.Scheduler.Provides {
  }
  
  
  @SuppressWarnings("all")
  public interface Parts {
  }
  
  
  @SuppressWarnings("all")
  public static class ComponentImpl implements fr.irit.smac.may.lib.components.scheduling.Scheduler.Component, fr.irit.smac.may.lib.components.scheduling.Scheduler.Parts {
    private final fr.irit.smac.may.lib.components.scheduling.Scheduler.Requires bridge;
    
    private final Scheduler implementation;
    
    protected void initParts() {
      
    }
    
    protected void initProvidedPorts() {
      assert this.tick == null;
      this.tick = this.implementation.make_tick();
      assert this.async == null;
      this.async = this.implementation.make_async();
      
    }
    
    public ComponentImpl(final Scheduler implem, final fr.irit.smac.may.lib.components.scheduling.Scheduler.Requires b, final boolean initMakes) {
      this.bridge = b;
      this.implementation = implem;
      
      assert implem.selfComponent == null;
      implem.selfComponent = this;
      
      // prevent them to be called twice if we are in
      // a specialized component: only the last of the
      // hierarchy will call them after everything is initialised
      if (initMakes) {
      	initParts();
      	initProvidedPorts();
      }
      
    }
    
    private Do tick;
    
    public final Do tick() {
      return this.tick;
    }
    
    private SchedulingControl async;
    
    public final SchedulingControl async() {
      return this.async;
    }
  }
  
  
  @SuppressWarnings("all")
  public abstract static class Scheduled {
    @SuppressWarnings("all")
    public interface Requires {
      /**
       * This can be called by the implementation to access this required port.
       * 
       */
      public Do cycle();
    }
    
    
    @SuppressWarnings("all")
    public interface Provides {
      /**
       * This can be called to access the provided port.
       * 
       */
      public Do stop();
    }
    
    
    @SuppressWarnings("all")
    public interface Component extends fr.irit.smac.may.lib.components.scheduling.Scheduler.Scheduled.Provides {
    }
    
    
    @SuppressWarnings("all")
    public interface Parts {
    }
    
    
    @SuppressWarnings("all")
    public static class ComponentImpl implements fr.irit.smac.may.lib.components.scheduling.Scheduler.Scheduled.Component, fr.irit.smac.may.lib.components.scheduling.Scheduler.Scheduled.Parts {
      private final fr.irit.smac.may.lib.components.scheduling.Scheduler.Scheduled.Requires bridge;
      
      private final fr.irit.smac.may.lib.components.scheduling.Scheduler.Scheduled implementation;
      
      protected void initParts() {
        
      }
      
      protected void initProvidedPorts() {
        assert this.stop == null;
        this.stop = this.implementation.make_stop();
        
      }
      
      public ComponentImpl(final fr.irit.smac.may.lib.components.scheduling.Scheduler.Scheduled implem, final fr.irit.smac.may.lib.components.scheduling.Scheduler.Scheduled.Requires b, final boolean initMakes) {
        this.bridge = b;
        this.implementation = implem;
        
        assert implem.selfComponent == null;
        implem.selfComponent = this;
        
        // prevent them to be called twice if we are in
        // a specialized component: only the last of the
        // hierarchy will call them after everything is initialised
        if (initMakes) {
        	initParts();
        	initProvidedPorts();
        }
        
      }
      
      private Do stop;
      
      public final Do stop() {
        return this.stop;
      }
    }
    
    
    private fr.irit.smac.may.lib.components.scheduling.Scheduler.Scheduled.ComponentImpl selfComponent;
    
    /**
     * Can be overridden by the implementation.
     * It will be called automatically after the component has been instantiated.
     * 
     */
    protected void start() {
      
    }
    
    /**
     * This can be called by the implementation to access the provided ports.
     * 
     */
    protected fr.irit.smac.may.lib.components.scheduling.Scheduler.Scheduled.Provides provides() {
      assert this.selfComponent != null;
      return this.selfComponent;
      
    }
    
    /**
     * This should be overridden by the implementation to define the provided port.
     * This will be called once during the construction of the component to initialize the port.
     * 
     */
    protected abstract Do make_stop();
    
    /**
     * This can be called by the implementation to access the required ports.
     * 
     */
    protected fr.irit.smac.may.lib.components.scheduling.Scheduler.Scheduled.Requires requires() {
      assert this.selfComponent != null;
      return this.selfComponent.bridge;
      
    }
    
    /**
     * This can be called by the implementation to access the parts and their provided ports.
     * 
     */
    protected fr.irit.smac.may.lib.components.scheduling.Scheduler.Scheduled.Parts parts() {
      assert this.selfComponent != null;
      return this.selfComponent;
      
    }
    
    /**
     * Not meant to be used to manually instantiate components (except for testing).
     * 
     */
    public fr.irit.smac.may.lib.components.scheduling.Scheduler.Scheduled.Component newComponent(final fr.irit.smac.may.lib.components.scheduling.Scheduler.Scheduled.Requires b) {
      fr.irit.smac.may.lib.components.scheduling.Scheduler.Scheduled.ComponentImpl comp = new fr.irit.smac.may.lib.components.scheduling.Scheduler.Scheduled.ComponentImpl(this, b, true);
      comp.implementation.start();
      return comp;
      
    }
    
    private fr.irit.smac.may.lib.components.scheduling.Scheduler.ComponentImpl ecosystemComponent;
    
    /**
     * This can be called by the species implementation to access the provided ports of its ecosystem.
     * 
     */
    protected fr.irit.smac.may.lib.components.scheduling.Scheduler.Provides eco_provides() {
      assert this.ecosystemComponent != null;
      return this.ecosystemComponent;
      
    }
    
    /**
     * This can be called by the species implementation to access the required ports of its ecosystem.
     * 
     */
    protected fr.irit.smac.may.lib.components.scheduling.Scheduler.Requires eco_requires() {
      assert this.ecosystemComponent != null;
      return this.ecosystemComponent.bridge;
      
    }
    
    /**
     * This can be called by the species implementation to access the parts of its ecosystem and their provided ports.
     * 
     */
    protected fr.irit.smac.may.lib.components.scheduling.Scheduler.Parts eco_parts() {
      assert this.ecosystemComponent != null;
      return this.ecosystemComponent;
      
    }
  }
  
  
  private fr.irit.smac.may.lib.components.scheduling.Scheduler.ComponentImpl selfComponent;
  
  /**
   * Can be overridden by the implementation.
   * It will be called automatically after the component has been instantiated.
   * 
   */
  protected void start() {
    
  }
  
  /**
   * This can be called by the implementation to access the provided ports.
   * 
   */
  protected fr.irit.smac.may.lib.components.scheduling.Scheduler.Provides provides() {
    assert this.selfComponent != null;
    return this.selfComponent;
    
  }
  
  /**
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract Do make_tick();
  
  /**
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract SchedulingControl make_async();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected fr.irit.smac.may.lib.components.scheduling.Scheduler.Requires requires() {
    assert this.selfComponent != null;
    return this.selfComponent.bridge;
    
  }
  
  /**
   * This can be called by the implementation to access the parts and their provided ports.
   * 
   */
  protected fr.irit.smac.may.lib.components.scheduling.Scheduler.Parts parts() {
    assert this.selfComponent != null;
    return this.selfComponent;
    
  }
  
  /**
   * Not meant to be used to manually instantiate components (except for testing).
   * 
   */
  public fr.irit.smac.may.lib.components.scheduling.Scheduler.Component newComponent(final fr.irit.smac.may.lib.components.scheduling.Scheduler.Requires b) {
    fr.irit.smac.may.lib.components.scheduling.Scheduler.ComponentImpl comp = new fr.irit.smac.may.lib.components.scheduling.Scheduler.ComponentImpl(this, b, true);
    comp.implementation.start();
    return comp;
    
  }
  
  /**
   * This should be overridden by the implementation to instantiate the implementation of the species.
   * 
   */
  protected abstract fr.irit.smac.may.lib.components.scheduling.Scheduler.Scheduled make_Scheduled();
  
  /**
   * Do not call, used by generated code.
   * 
   */
  public fr.irit.smac.may.lib.components.scheduling.Scheduler.Scheduled _createImplementationOfScheduled() {
    fr.irit.smac.may.lib.components.scheduling.Scheduler.Scheduled implem = make_Scheduled();
    assert implem.ecosystemComponent == null;
    assert this.selfComponent != null;
    implem.ecosystemComponent = this.selfComponent;
    return implem;
  }
}
