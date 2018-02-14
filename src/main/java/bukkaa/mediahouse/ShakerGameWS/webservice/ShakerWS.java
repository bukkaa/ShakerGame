package bukkaa.mediahouse.ShakerGameWS.webservice;

import bukkaa.mediahouse.ShakerGameWS.Main;
import bukkaa.mediahouse.ShakerGameWS.controller.GameController;
import bukkaa.mediahouse.ShakerGameWS.helpers.FileAccessHelper;
import bukkaa.mediahouse.ShakerGameWS.model.GamerAccelerationData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ShakerWS {
    private static final Logger log = LoggerFactory.getLogger(ShakerWS.class);

    private static GameController controller = Main.getGameController();

    @GetMapping("/")
    public String renderStartPage(Model model){
        model.addAttribute("sprites", FileAccessHelper.getSpriteMap());
        model.addAttribute("background_img", FileAccessHelper.getBackgroundImageWebPath());
        return "index";
    }

    @PostMapping("/sign_in")
    public @ResponseBody String signUpShakeGamer(@RequestParam("name") String name,
                                                 @RequestParam("texture") String texture) {
        log.debug("New sign in data: name: <{}> texture: {}", name, texture);

        int id = controller.signUpNewGamer(name, texture);
        return String.valueOf(id);
    }

    @PostMapping("/play")
    public @ResponseBody String readData(@RequestParam("id") String id,
                                         @RequestParam("ax") float ax,
                                         @RequestParam("ay") float ay) {
        return controller.writeAccelerationData(new GamerAccelerationData(Integer.parseInt(id.trim()), ax, ay));
    }
}