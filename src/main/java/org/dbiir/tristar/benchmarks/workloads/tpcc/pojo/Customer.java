/*
 * Copyright 2020 by OLTPBenchmark Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.dbiir.tristar.benchmarks.workloads.tpcc.pojo;

import java.sql.Timestamp;
import java.util.Objects;

public class Customer {

  public int c_id;
  public int c_d_id;
  public int c_w_id;
//  public int c_payment_cnt;
//  public int c_delivery_cnt;
//  public Timestamp c_since;
//  public float c_discount;
//  public float c_credit_lim;
  public float c_balance;
//  public float c_ytd_payment;
//  public String c_credit;
//  public String c_last;
//  public String c_first;
//  public String c_street_1;
//  public String c_street_2;
//  public String c_city;
//  public String c_state;
//  public String c_zip;
//  public String c_phone;
//  public String c_middle;
//  public String c_data;
  public String c_info;

  @Override
  public String toString() {
    return ("\n***************** Customer ********************"
        + "\n*           c_id = "
        + c_id
        + "\n*         c_d_id = "
        + c_d_id
        + "\n*         c_w_id = "
        + c_w_id
        + "\n*      c_balance = "
        + c_balance
        + "\n*      c_info = "
        + c_info
        + "\n**********************************************");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Customer customer = (Customer) o;
    return c_id == customer.c_id
        && c_d_id == customer.c_d_id
        && c_w_id == customer.c_w_id
//        && c_payment_cnt == customer.c_payment_cnt
//        && c_delivery_cnt == customer.c_delivery_cnt
//        && Float.compare(customer.c_discount, c_discount) == 0
//        && Float.compare(customer.c_credit_lim, c_credit_lim) == 0
        && Float.compare(customer.c_balance, c_balance) == 0
        && customer.c_info.equals(c_info);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        c_id,
        c_d_id,
        c_w_id,
//        c_payment_cnt,
//        c_delivery_cnt,
//        c_since,
//        c_discount,
//        c_credit_lim,
        c_balance,
//        c_ytd_payment,
//        c_credit,
//        c_last,
//        c_first,
//        c_street_1,
//        c_street_2,
//        c_city,
//        c_state,
//        c_zip,
//        c_phone,
//        c_middle,
        c_info);
  }
}
