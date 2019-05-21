package proj.simulacao;

import java.util.HashMap;

import eduni.simjava.*;

public class Main {

    @SuppressWarnings("unused")
	public static void main(String[] args) {
      
      // Initialize Sim_system
      Sim_system.initialise();
      
      // Operational vars
      long seed = 123;
      HashMap<String, Double[]> probPorts;
      Double[] probValues;

      // Source
      Source source = new Source("Source", 40, 3, seed);
      
      // CPU Web Server
      probPorts = new HashMap<String, Double[]>();
      
      probValues = new Double[] {0.5, 1.0};
      probPorts.put("Source", probValues);
      
      probValues = new Double[] {0.80, 0.90};
      probPorts.put("CpuApplication", probValues);
      
      probValues = new Double[] {0.10};
      probPorts.put("DiskWebServer", probValues);
      
      CPU cpu1 = new CPU("CpuWebServer", 5, 1, seed, probPorts, Scope.WebServer);
      
      // CPU Application
      probPorts = new HashMap<String, Double[]>();
      
      probValues = new Double[] {0.05, 0.38};
      probPorts.put("CpuWebServer", probValues);
      
      probValues = new Double[] {0.85, 0.95};
      probPorts.put("CpuDatabase", probValues);
      
      probValues = new Double[] {0.40};
      probPorts.put("DiskApplication", probValues);
      
      CPU cpu2 = new CPU("CpuApplication", 6, 1.5, seed, probPorts, Scope.Application);
      
      // CPU Database
      probPorts = new HashMap<String, Double[]>();
      
      probValues = new Double[] {0.10, 0.95};
      probPorts.put("CpuApplication", probValues);
      
      probValues = new Double[] {0.40};
      probPorts.put("Cache", probValues);
      
      CPU cpu3 = new CPU("CpuDatabase", 4, 0.2, seed, probPorts, Scope.Database);
      
      // Cache
      Cache cache = new Cache("Cache", 5, 2, seed, 0.70);
      
      // Disks
      Disk disk = new Disk("DiskWebServer", 4, 0.6, seed, Scope.WebServer);
      Disk disk2 = new Disk("DiskApplication", 4, 0.6, seed, Scope.Application);
      Disk disk3 = new Disk("DiskDatabase", 4, 0.6, seed, Scope.Database);
      
      // Out
      Out out = new Out("Output", 5, 2, seed);
            
      // Source ports
      Sim_system.link_ports("Source", "SourceOut1", "CpuWebServer", "CpuIn2");
      
      // Cpu Ports from WebServer
      Sim_system.link_ports("CpuWebServer", "CpuOut1", "CpuApplication", "CpuIn1");
      Sim_system.link_ports("CpuWebServer", "CpuOut2", "Output", "OutputIn1");
      Sim_system.link_ports("CpuWebServer", "CpuOut3", "DiskWebServer", "DiskIn1");

      // Disk Ports from WebServer
      Sim_system.link_ports("DiskWebServer", "DiskOut1", "CpuWebServer", "CpuIn3");
      
      // Cpu Ports from Application
      Sim_system.link_ports("CpuApplication", "CpuOut1", "CpuWebServer", "CpuIn1");
      Sim_system.link_ports("CpuApplication", "CpuOut2", "CpuDatabase", "CpuIn1");
      Sim_system.link_ports("CpuApplication", "CpuOut3", "DiskApplication", "DiskIn1");
      
      // Disk Ports from Application
      Sim_system.link_ports("DiskApplication", "DiskOut1", "CpuApplication", "CpuIn3");
      
      // Cpu Ports from Database
      Sim_system.link_ports("CpuDatabase", "CpuOut1", "CpuApplication", "CpuIn2");
      Sim_system.link_ports("CpuDatabase", "CpuOut2", "DiskDatabase", "DiskIn2");
      Sim_system.link_ports("CpuDatabase", "CpuOut3", "Cache", "CacheIn1");
      
      // Cache Ports from Database
      Sim_system.link_ports("Cache", "CacheOut1", "CpuDatabase", "CpuIn3");
      Sim_system.link_ports("Cache", "CacheOut2", "DiskDatabase", "DiskIn1");
      
      // Disk Ports from Database
      Sim_system.link_ports("DiskDatabase", "DiskOut1", "CpuDatabase", "CpuIn2");
    
      // Configure trace to the simulator (default, entity, event)
      Sim_system.set_trace_detail(false, true, false);
      Sim_system.set_report_detail(true, false);
      
      // Run the simulation
      Sim_system.run();
    }
  }