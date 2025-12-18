package org.dbiir.tristar.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Partition {
  private int id;
  private int weight;
  private double zipf;
  private double wrtup;
  private double wrtxn;

  public Partition(int id, int weight, double zipf, double wrtup, double wrtxn) {
    this.id = id;
    this.weight = weight;
    this.zipf = zipf;
    this.wrtup = wrtup;
    this.wrtxn = wrtxn;
  }

  @Override
  public String toString() {
    return String.format("Partition{id=%d, zipf=%.1f, wrtup=%.1f, wrtxn=%.1f}",
            id, zipf, wrtup, wrtxn);
  }
}
