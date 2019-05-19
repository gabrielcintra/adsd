package proj.simulacao;

import eduni.simjava.*;
import eduni.simjava.distributions.*;

class Disk extends Sim_entity {
  
  Sim_stat stat;
  private Sim_port in1, in2, out1;
  private Sim_normal_obj delay;
  private Origin origin;

  public Disk(String name, double mean, double variance, long seed, Origin origin) {
    super(name);
    this.delay = new Sim_normal_obj("Delay", mean, variance, seed);
    this.origin = origin;
    
    stat = new Sim_stat();
    stat.add_measure(Sim_stat.ARRIVAL_RATE);        
    stat.add_measure(Sim_stat.QUEUE_LENGTH);    
    stat.add_measure(Sim_stat.RESIDENCE_TIME);
    stat.add_measure(Sim_stat.SERVICE_TIME);    
    stat.add_measure(Sim_stat.THROUGHPUT);
    stat.add_measure(Sim_stat.UTILISATION);
    stat.add_measure(Sim_stat.WAITING_TIME);    
    set_stat(stat);  
    
    // Input and output ports
    in1 = new Sim_port("DiskIn1");
    in2 = new Sim_port("DiskIn2");
    out1 = new Sim_port("DiskOut1");
    
    // Minimum ports
    add_port(in1);
    add_port(out1);
    
    // If its from WebServer scope, it uses cache as input
    if (this.origin == Origin.Database) 
    	add_port(in2);
  }

  public void body() {
    while (Sim_system.running()) {
      Sim_event e = new Sim_event();

      sim_get_next(e);
      double delaySample = delay.sample();
      sim_trace(1, "Database service started. Delay: " + delaySample);      
      sim_process(delaySample);
      
      sim_completed(e);
      
      try {
    	  if (e.get_src() != -1) {    		      	  
	    	  String origin = Sim_system.get_entity(e.get_src()).get_name();
	    	  sim_trace(1, "Disk responds to " + origin);
	    	  sim_schedule(out1, 0.0, 1);
    	  }
      } catch(eduni.simjava.Sim_exception exc) {
    	  System.out.println("Algo errado aconteceu!");
    	  System.out.println(exc.getMessage());    	  
      }
                      
    }
  }
}