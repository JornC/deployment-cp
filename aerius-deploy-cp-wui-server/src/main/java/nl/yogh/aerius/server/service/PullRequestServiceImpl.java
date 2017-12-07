package nl.yogh.aerius.server.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.essentials4j.New;

import nl.yogh.aerius.builder.domain.ProductInfo;
import nl.yogh.aerius.builder.domain.ProductType;
import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.builder.domain.ServiceStatus;
import nl.yogh.aerius.builder.service.ProductDeploymentAction;
import nl.yogh.aerius.builder.service.PullRequestService;

public class PullRequestServiceImpl implements PullRequestService {
  @Override
  public ArrayList<PullRequestInfo> getPullRequests() {
    final ArrayList<PullRequestInfo> lst = new ArrayList<>();

    lst.add(PullRequestInfo.create().title("OL4 refactor").author("JornC").idx("654")
        .products(new HashMap<>(New.map(ProductType.CALCULATOR, ProductInfo.create().hash("5c7daa").status(ServiceStatus.UNBUILT),
            ProductType.SCENARIO, ProductInfo.create().hash("3b6a52").status(ServiceStatus.SUSPENDED), ProductType.WORKERS,
            ProductInfo.create().hash("aa2e1f").status(ServiceStatus.UNBUILT)))));
    lst.add(PullRequestInfo.create().title("RWS Item road base layer").author("JohnV").idx("653")
        .products(new HashMap<>(New.map(ProductType.CALCULATOR, ProductInfo.create().hash("d8e1db").status(ServiceStatus.RUNNING),
            ProductType.SCENARIO, ProductInfo.create().hash("3b6a52").status(ServiceStatus.SUSPENDED), ProductType.WORKERS,
            ProductInfo.create().hash("aa2e1f").status(ServiceStatus.UNBUILT)))));
    lst.add(PullRequestInfo.create().title("Only lookup source if we have imear").author("JohnV").idx("652")
        .products(new HashMap<>(New.map(ProductType.CALCULATOR, ProductInfo.create().hash("5c7daa").status(ServiceStatus.UNBUILT),
            ProductType.SCENARIO, ProductInfo.create().hash("3b6a52").status(ServiceStatus.SUSPENDED), ProductType.WORKERS,
            ProductInfo.create().hash("aa2e1f").status(ServiceStatus.UNBUILT)))));
    lst.add(PullRequestInfo.create().title("User geo layer batch inserter").author("JacobusXIII").idx("651")
        .products(new HashMap<>(New.map(
            ProductType.CALCULATOR, ProductInfo.create().hash("d8e1db").status(ServiceStatus.RUNNING),
            ProductType.SCENARIO, ProductInfo.create().hash("3b6a52").status(ServiceStatus.SUSPENDED),
            ProductType.WORKERS, ProductInfo.create().hash("aa2e1f").status(ServiceStatus.UNBUILT),
            ProductType.TESTS, ProductInfo.create().hash("e1dbd8").status(ServiceStatus.UNBUILT)))));

    return lst;
  }

  @Override
  public ProductInfo doAction(final ProductDeploymentAction action, final ProductType type, final ProductInfo info) {
    info.busy(true);
    return info;
  }
}
