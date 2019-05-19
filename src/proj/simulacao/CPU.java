package proj.simulacao;

import eduni.simjava.*;
import eduni.simjava.distributions.*;

class CPU extends Sim_entity {
  
	 Scope type;
	 Sim_stat stat;
	 private Sim_normal_obj delay;
	 private Sim_random_obj prob;
	 private Sim_port in1, in2, in3,  
   				   	  out1, out2, out3;
	 
	 public CPU (String name, double mean, double variance, long seed, Scope type) {
		super(name);
		this.delay = new Sim_normal_obj("Delay", mean, variance, seed);
		this.prob = new Sim_random_obj("Probability", seed);
		this.type = type;
	    
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
	    in1 = new Sim_port("CpuIn1");
	    in2 = new Sim_port("CpuIn2");
	    in3 = new Sim_port("CpuIn3");
	    
	    // Output ports
	    out1 = new Sim_port("CpuOut1");
	    out2 = new Sim_port("CpuOut2");  
	    out3 = new Sim_port("CpuOut3");  
	    
	    // Minimum ports
	    add_port(in1);
	    add_port(in2);
	    add_port(in3);
	    add_port(out1);
	    add_port(out2);
	    add_port(out3);
	  }
		
	  public void body() {
	    while (Sim_system.running()) {
	      Sim_event e = new Sim_event();
	      // Get the next event
	      sim_get_next(e);
	            
	      double delaySample = delay.sample(),
	    		 probSample = prob.sample();
	      
	      sim_trace(1, this.type + " CPU request started. Delay: " + delaySample);
	      // Process the event
	      sim_process(delaySample);
	      // The event has completed service
	      sim_completed(e);
	      
	      try {
	    	  if (e.get_src() != -1) {    		  
		    	  String origin = Sim_system.get_entity(e.get_src()).get_name();  

		          if (this.type == Scope.WebServer) { // WebServer Scope
			    	  if (origin.equals("Source")) { 
			    		  if (probSample < 0.50) {
				          	sim_trace(1, "WebServer Disk selected to receive the request.");
				          	sim_schedule(out3, 0.0, 1);
				          } else {
				          	sim_trace(1, "Application CPU selected to receive the request.");
				            sim_schedule(in1, 0.0, 1);
				          }
			          } else if (origin.equals("CpuApplication")) {  	  
			    		  if (probSample < 0.80) {
				          	sim_trace(1, "Output selected to receive the request.");
				          	sim_schedule(out2, 0.0, 1);
			    		  } else if (probSample < 0.90) {
				          	sim_trace(1, "WebServer Disk selected to receive the request.");
				          	sim_schedule(out3, 0.0, 1);
				          } else {
				          	sim_trace(1, "Application CPU selected to receive the request.");
				            sim_schedule(out1, 0.0, 1);
				          }
			          } else if (origin.equals("DiskWebServer")) {  	  
			    		  if (probSample < 0.10) {
				          	sim_trace(1, "Output selected to receive the request.");
				          	sim_schedule(out2, 0.0, 1);
				          } else {
				          	sim_trace(1, "Application CPU selected to receive the request.");
				            sim_schedule(out1, 0.0, 1);    
				          }
			          }    	
		          }
	    	  
		          else if (this.type == Scope.Application) { // Application Scope
			    	  if (origin.equals("CpuWebServer")) {  
			    		  if (probSample < 0.05) {
				          	sim_trace(1, "WebServer CPU selected to receive the request.");
				          	sim_schedule(out1, 0.0, 1);
				          } else if (probSample < 0.38) {
				          	sim_trace(1, "Application Disk selected to receive the request.");
				          	sim_schedule(out3, 0.0, 1);
					      } else {
				          	sim_trace(1, "Database CPU selected to receive the request.");
				            sim_schedule(out2, 0.0, 1);
				          }
			          } else if (origin.equals("CpuDatabase")) {  	  
			    		  if (probSample < 0.85) {
				          	sim_trace(1, "WebServer CPU selected to receive the request.");
				          	sim_schedule(out1, 0.0, 1);
				          } else if (probSample < 0.95) {
				          	sim_trace(1, "Application Disk selected to receive the request.");
				          	sim_schedule(out3, 0.0, 1);
					      } else {
				          	sim_trace(1, "Database CPU selected to receive the request.");
				            sim_schedule(out2, 0.0, 1);
				          } 	  
			          } else if (origin.equals("DiskApplication")) {  
			    		  if (probSample < 0.40) {
				          	sim_trace(1, "WebServer CPU selected to receive the request.");
				          	sim_schedule(out1, 0.0, 1);
				          } else {
				          	sim_trace(1, "Database CPU selected to receive the request.");
				            sim_schedule(out2, 0.0, 1);
				          }
			          }
		          }
		          
		          else if (this.type == Scope.Database) { // Database Scope
			    	  if (origin.equals("CpuApplication")) {  
			    		  if (probSample < 0.10) {
				          	sim_trace(1, "Application CPU selected to receive the request.");
				          	sim_schedule(out1, 0.0, 1);
				          } else if (probSample < 0.95) {
				          	sim_trace(1, "Cache selected to receive the request.");
				          	sim_schedule(out3, 0.0, 1);
					      } else {
				          	sim_trace(1, "Database Disk to receive the request.");
				            sim_schedule(out3, 0.0, 1);
				          }
			          } else if (origin.equals("Cache")) {  	  
			    		  if (probSample < 0.40) {
				          	sim_trace(1, "Application CPU selected to receive the request.");
				          	sim_schedule(out1, 0.0, 1);
				          } else {
				          	sim_trace(1, "Database Disk selected to receive the request.");
				            sim_schedule(out2, 0.0, 1);
				          }	  
			          } else {  
				          sim_trace(1, "Application CPU selected to receive the request.");
				          sim_schedule(out1, 0.0, 1);
			          }
		          }
	    	  }
	      } catch(eduni.simjava.Sim_exception exc) {
	    	  System.out.println("Something went wrong!");
	    	  System.out.println(exc.getMessage());
	      }
	      
	    }
	  }
}