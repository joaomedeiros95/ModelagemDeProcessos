package br.ufrn.info.bpm.progressao.teste;

import br.ufrn.info.bpm.api.BPMIntegrityException;
import com.google.gson.Gson;
import spark.ResponseTransformer;

import static spark.Spark.after;
import static spark.Spark.get;

public class RESTServer {

    public RESTServer(MainProgressao mainProgressao) {
        get("/processos", (request, response) -> mainProgressao.passoAtual, json());

        after((req, res) -> {
            res.type("application/json");
        });
    }

    public static class JsonUtil {
        public static String toJson(Object object) {
            return new Gson().toJson(object);
        }
    }

    public static ResponseTransformer json() {
        return JsonUtil::toJson;
    }

    public static void main(String[] args) throws BPMIntegrityException {
        final MainProgressao mainProgressao = new MainProgressao();
        new RESTServer(mainProgressao);
        mainProgressao.start();
    }

}
