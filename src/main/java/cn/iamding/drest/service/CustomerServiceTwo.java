package cn.iamding.drest.service;


import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import cn.iamding.drest.annotation.RestPath;

@RestPath("/customer2")
public class CustomerServiceTwo {
    private Map<Long, Customer> customerMap = new HashMap<>();
    private AtomicLong seq = new AtomicLong(1);

    @RestPath("/create")
    public Customer create(Customer form) {
        form.setId(seq.getAndIncrement());
        form.setCts(new Date());

        customerMap.put(form.getId(), form);

        return form;
    }

    @RestPath("/get")
    public Customer get(Idable id) {
        return customerMap.get(id.getId());
    }

    @RestPath("/list")
    public Collection<Customer> list() {
        return customerMap.values();
    }

    public Collection<Customer> listNoRest() {
        return customerMap.values();
    }

    public static class Idable {
        private long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }

    public static class Customer extends Idable {
        private int age;
        private String name;
        private Date cts;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getCts() {
            return cts;
        }

        public void setCts(Date cts) {
            this.cts = cts;
        }
    }
}
