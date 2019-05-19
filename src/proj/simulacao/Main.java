package proj.simulacao;

import eduni.simjava.*;

public class Main {

    @SuppressWarnings("unused")
	public static void main(String[] args) {
      // Initialize Sim_system
      Sim_system.initialise();
      
      long seed = 123;

      // Entities
      Source source = new Source("Source", 40, 3, seed);
      
      CPU cpu1 = new CPU("CpuWebServer", 5, 1, seed, Scope.WebServer);
      CPU cpu2 = new CPU("CpuApplication", 6, 1.5, seed, Scope.Application);
      CPU cpu3 = new CPU("CpuDatabase", 4, 0.2, seed, Scope.Database);
      
      Cache cache = new Cache("Cache", 5, 2, seed);
      
      Disk disk = new Disk("DiskWebServer", 1, 0.8, seed, Scope.WebServer);
      Disk disk2 = new Disk("DiskApplication", 2, 1, seed, Scope.Application);
      Disk disk3 = new Disk("DiskDatabase", 3, 1.5, seed, Scope.Database);
      
      Out out = new Out("Output", 5, 2, seed);
            
      // Source ports
      Sim_system.link_ports("Source", "SourceOut1", "CpuWebServer", "CpuIn2");
      
      // Cpu from WebServer
      Sim_system.link_ports("CpuWebServer", "CpuOut1", "CpuApplication", "CpuIn1");
      Sim_system.link_ports("CpuWebServer", "CpuOut2", "Output", "OutputIn1");
      Sim_system.link_ports("CpuWebServer", "CpuOut3", "DiskWebServer", "DiskIn1");

      // Disk from WebServer
      Sim_system.link_ports("DiskWebServer", "DiskOut1", "CpuWebServer", "CpuIn3");
      
      // Cpu from Application
      Sim_system.link_ports("CpuApplication", "CpuOut1", "CpuWebServer", "CpuIn1");
      Sim_system.link_ports("CpuApplication", "CpuOut2", "CpuDatabase", "CpuIn1");
      Sim_system.link_ports("CpuApplication", "CpuOut3", "DiskApplication", "DiskIn1");
      
      // Disk from Application
      Sim_system.link_ports("DiskApplication", "DiskOut1", "CpuApplication", "CpuIn3");
      
      // Cpu from Database
      Sim_system.link_ports("CpuDatabase", "CpuOut1", "CpuApplication", "CpuIn2");
      Sim_system.link_ports("CpuDatabase", "CpuOut2", "DiskDatabase", "DiskIn2");
      Sim_system.link_ports("CpuDatabase", "CpuOut3", "Cache", "CacheIn1");
      
      // Cache from Database
      Sim_system.link_ports("Cache", "CacheOut1", "CpuDatabase", "CpuIn3");
      Sim_system.link_ports("Cache", "CacheOut2", "DiskDatabase", "DiskIn1");
      
      // Disk from Database
      Sim_system.link_ports("DiskDatabase", "DiskOut1", "CpuDatabase", "CpuIn2");
    
      // Configure trace to the simulator (default, entity, event)
      Sim_system.set_trace_detail(false, true, false);
      Sim_system.set_report_detail(true, false);
      
      // Run the simulation
      Sim_system.run();
    }
  }