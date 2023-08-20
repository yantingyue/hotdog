package org.example;

import org.example.yty.domain.SellingTaskRequestDTO;
import org.example.yty.service.SellingTaskProcessService;

public class SellingTaskProcessServiceTest {
    public static void main(String[] args) throws InterruptedException {
        SellingTaskProcessService sellingTaskProcessService = new SellingTaskProcessService();
        sellingTaskProcessService.init();

        String str = "16e82b22f8524ed6b1b16db36804c71f,1019327,1321,725396,50";

        //多个这样子用不&连起来 "16e82b22f8524ed6b1b16db36804c71f,1019327,1321,725396,50&16e82b22f8524ed6b1b16db36804c71f,1019327,1321,725396,50";

        String[] split = str.split("&");

        for (String val : split) {
            String[] split1 = val.split(",");
            if (split1.length != 5) {
                System.out.println("配置错误：" + val);
                continue;
            }
            SellingTaskRequestDTO build = SellingTaskRequestDTO.builder()
                    .token(split1[0])
                    .sellSize(Integer.valueOf(split1[4]))
                    .productId(Integer.valueOf(split1[1]))
                    .nft_product_size_id(Integer.valueOf(split1[2]))
                    .pwd(Integer.valueOf(split1[3]))
                    .build();
            sellingTaskProcessService.addQueue(build);
        }
    }
}
