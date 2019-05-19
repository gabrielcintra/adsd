package proj.simulacao;

import eduni.simjava.*;
import eduni.simjava.distributions.*;

class Cache extends Sim_entity {
  private Sim_port in1, out1, out2;
  private Sim_normal_obj delay;
  private Sim_random_obj prob;
  Sim_stat stat;

  public Cache(String name, double mean, double variance, long seed) {
    super(name);
    this.delay = new Sim_normal_obj("Delay", mean, variance, seed);
    this.prob = new Sim_random_obj("Probability", seed);
    
    stat = new Sim_stat();
    stat.add_measure(Sim_stat.ARRIVAL_RATE);        
    stat.add_measure(Sim_stat.QUEUE_LENGTH);    
    stat.add_measure(Sim_stat.RESIDENCE_TIME);
    stat.add_measure(Sim_stat.SERVICE_TIME);    
    stat.add_measure(Sim_stat.THROUGHPUT);
    stat.add_measure(Sim_stat.UTILISATION);
    stat.add_measure(Sim_stat.WAITING_TIME);    
    set_stat(stat);    
    
    // Input ports 
    in1 = new Sim_port("CacheIn1");
    
    // Output ports  
    out1 = new Sim_port("CacheOut1");    
    out2 = new Sim_port("CacheOut2"); 
    
    // Registering ports
    add_port(in1);
    add_port(out1);
    add_port(out2);
  }

  public void body() {
    while (Sim_system.running()) {
      Sim_event e = new Sim_event();
      // Get the next event
      sim_get_next(e);
      // Process the event
      double delaySample = delay.sample();
      sim_trace(1, "Cache started. Delay: " + delaySample);
      sim_process(delaySample);
      // The event has completed service
      sim_completed(e);
      
      double probSample = prob.sample();

      if (probSample < 0.70) {
    	sim_trace(1, "Database CPU selected to receive the request.");
    	sim_schedule(out1, 0.0, 1);
      } else {       
    	sim_trace(1, "Database Disk selected to receive the request.");
    	sim_schedule(out2, 0.0, 1);      
      }      
    }
  }
}