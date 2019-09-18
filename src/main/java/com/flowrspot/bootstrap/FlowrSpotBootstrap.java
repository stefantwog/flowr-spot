package com.flowrspot.bootstrap;

import com.flowrspot.domain.Flower;
import com.flowrspot.repository.FlowerRepository;
import com.flowrspot.service.FlowerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FlowrSpotBootstrap implements CommandLineRunner {

    private final FlowerRepository flowerRepository;

    public FlowrSpotBootstrap(FlowerRepository flowerRepository) {
        this.flowerRepository = flowerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(flowerRepository.count()==0){
            initFlowers();
        }
    }

    private void initFlowers() {
        List<Flower> flowers = new ArrayList<>();

        Flower rose = new Flower();
        rose.setName("Rose");
        rose.setDescription("This is Rose");
        rose.setImage("https://cdn.pixabay.com/photo/2018/11/08/12/02/rose-3802424__340.jpg");
        flowers.add(rose);

        Flower carnation = new Flower();
        carnation.setName("Carnation");
        carnation.setDescription("This is Carnation");
        carnation.setImage("https://assets.teleflora.com/images/customhtml/meaning-of-flowers/carnation.png");
        flowers.add(carnation);

        Flower tulip = new Flower();
        tulip.setName("Tulip");
        tulip.setDescription("This is Tulip");
        tulip.setImage("https://images.homedepot-static.com/productImages/f8fb8acc-434c-4524-b56a-355b1d9bcc32/svn/garden-state-bulb-flower-bulbs-hof18-08-64_1000.jpg");
        flowers.add(tulip);

        Flower daisy = new Flower();
        daisy.setName("Daisy");
        daisy.setDescription("This is Daisy");
        daisy.setImage("https://www.naturescape.co.uk/wp-content/uploads/2016/09/Commondaisy1-e1523014031565.jpg");
        flowers.add(daisy);

        Flower sunflower = new Flower();
        sunflower.setName("Sunflower");
        sunflower.setDescription("This is Sunflower");
        sunflower.setImage("https://sarahraven.images.blucommerce.com/sarahraven/product/020016_1.jpg?auto=format%2Ccompress&bluhash=1b0158c69e8a76b58df6cead54f90b55&w=500&h=550&fit=crop&s=c04d385bf622025ededcf519e559358e");
        flowers.add(sunflower);

        Flower daffodil = new Flower();
        daffodil.setName("Daffodil");
        daffodil.setDescription("This is Daffodil");
        daffodil.setImage("https://images.homedepot-static.com/productImages/d43fcef4-ab0a-45a3-9862-aa2f2a0bbaa2/svn/garden-state-bulb-flower-bulbs-hof18-10-64_1000.jpg");
        flowers.add(daffodil);

        Flower orchid = new Flower();
        orchid.setName("Orchid");
        orchid.setDescription("This is Orchid");
        orchid.setImage("https://img.teleflora.com/images/o_0/l_flowers:TPL05-1A,pg_6/w_368,h_460,cs_no_cmyk,c_pad/f_jpg,q_auto:eco,e_sharpen:200/flowers/TPL05-1A/GloriousGratitudeOrchid");
        flowers.add(orchid);

        Flower iris = new Flower();
        iris.setName("Iris");
        iris.setDescription("This is Iris");
        iris.setImage("https://q7i2y6d5.stackpathcdn.com/wp-content/uploads/2011/08/iris-flowers1-400x342.jpg");
        flowers.add(iris);

        Flower gardenia = new Flower();
        gardenia.setName("Gardenia");
        gardenia.setDescription("This is Gardenia");
        gardenia.setImage("https://images.homedepot-static.com/productImages/2833fafa-4dc4-4420-9873-69a9ee207433/svn/shrubs-20973fl-64_1000.jpg");
        flowers.add(gardenia);

        Flower jasmine = new Flower();
        jasmine.setName("Jasmine");
        jasmine.setDescription("This is Jasmine");
        jasmine.setImage("https://www.lgbotanicals.com/assets/images/jasminum-grandiflorum-736a.jpg");
        flowers.add(jasmine);

        flowerRepository.save(flowers);
    }
}
