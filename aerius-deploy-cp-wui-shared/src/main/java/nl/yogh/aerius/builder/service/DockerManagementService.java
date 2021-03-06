package nl.yogh.aerius.builder.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import nl.yogh.aerius.builder.domain.DockerContainer;
import nl.yogh.aerius.builder.domain.DockerImage;
import nl.yogh.aerius.builder.exception.ApplicationException;

@RemoteServiceRelativePath(ServiceURLConstants.DOCKER_GWT_PATH)
public interface DockerManagementService extends RemoteService {
  void stopAllContainers() throws ApplicationException;

  void removeAllContainers() throws ApplicationException;

  void removeAllNetworks() throws ApplicationException;

  void removeAllImages() throws ApplicationException;

  void purgeTracker() throws ApplicationException;

  ArrayList<DockerImage> retrieveImages() throws ApplicationException;

  boolean removeImage(DockerImage image) throws ApplicationException;

  ArrayList<DockerContainer> retrieveContainers() throws ApplicationException;

  boolean stopContainer(DockerContainer container) throws ApplicationException;

  boolean removeContainer(DockerContainer container) throws ApplicationException;

  HashMap<String, String> retrieveStats() throws ApplicationException;
}
