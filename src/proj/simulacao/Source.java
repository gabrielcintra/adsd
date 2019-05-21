package proj.simulacao;

import eduni.simjava.*;        // Import the SimJava basic package
import eduni.simjava.distributions.*;

class Source extends Sim_entity {
	
	private Sim_port out;
    private Sim_normal_obj delay;
    Sim_stat stat;

    public Source(String name, double mean, double variance, long seed) {
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
      
      // Port for sending requests to the first CPU
      out = new Sim_port("SourceOut1");
      add_port(out);
    }
  
  public void body() {
      for (int i=0; i < 100; i++) {
        // Send the CPU a job
        sim_schedule(out, 0.0, 0);
        double delaySample = delay.sample();
        sim_trace(1, "New request from Source. Delay: " + delaySample);        
        // Pause
        sim_pause(delaySample);
      }
    }
}