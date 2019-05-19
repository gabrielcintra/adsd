package proj.simulacao;

import eduni.simjava.*;
import eduni.simjava.distributions.*;

class Out extends Sim_entity {
  private Sim_port in;
  private Sim_normal_obj delay;
  Sim_stat stat;

  public Out(String name, double mean, double variance, long seed) {
    super(name);
    this.delay = new Sim_normal_obj("Delay", mean, variance, seed);
    
    stat = new Sim_stat();
    stat.add_measure(Sim_stat.ARRIVAL_RATE);        
    stat.add_measure(Sim_stat.QUEUE_LENGTH);    
    stat.add_measure(Sim_stat.RESIDENCE_TIME);
    stat.add_measure(Sim_stat.SERVICE_TIME);    
    stat.add_measure(Sim_stat.THROUGHPUT);
    stat.add_measure(Sim_stat.UTILISATION);
    stat.add_measure(Sim_stat.WAITING_TIME);    
    set_stat(stat);  
    
    in = new Sim_port("OutputIn1");
    add_port(in);
  }

  public void body() {
    while (Sim_system.running()) {
      Sim_event e = new Sim_event();

      sim_get_next(e);
      double delaySample = delay.sample();
      sim_trace(1, "General output started. Delay: " + delaySample);      
      sim_process(delaySample);

      sim_completed(e);
      
      try {
    	  if (e.get_src() != -1) {
    		  sim_trace(1, "Output completed: " + Sim_system.get_entity(e.get_src()).get_name() );
    	  }
      }catch(eduni.simjava.Sim_exception exc) {
    	  System.out.println("Something went wrong!");
    	  System.out.println(exc.getMessage());
      }
            
    }
  }
}