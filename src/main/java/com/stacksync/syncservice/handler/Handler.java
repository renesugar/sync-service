package com.stacksync.syncservice.handler;

import com.stacksync.commons.exceptions.ShareProposalNotCreatedException;
import com.stacksync.commons.exceptions.UserNotFoundException;
import com.stacksync.commons.models.CommitInfo;
import com.stacksync.syncservice.db.Connection;
import com.stacksync.syncservice.db.ConnectionPool;
import com.stacksync.syncservice.db.DAOFactory;
import com.stacksync.syncservice.db.infinispan.GlobalDAO;
import com.stacksync.syncservice.db.infinispan.models.*;
import com.stacksync.syncservice.exceptions.InternalServerError;
import com.stacksync.syncservice.exceptions.dao.DAOException;
import com.stacksync.syncservice.util.Config;
import org.apache.log4j.Logger;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Handler{

   private static final Logger logger = Logger.getLogger(Handler.class.getName());

   protected Connection connection;
   protected GlobalDAO globalDAO;

   protected static Random random = new Random(System.currentTimeMillis());

   public Handler(ConnectionPool pool) throws Exception {
      connection = pool.getConnection();
      String dataSource = Config.getDatasource();
      DAOFactory factory = new DAOFactory(dataSource);
      globalDAO = factory.getGlobalDAO(connection);
   }
   
   public void createUser(UUID id) throws Exception {
       UserRMI user = new UserRMI(id, id.toString(), id.toString(), null, "a@a.a", 0, 0);
       globalDAO.add(user);
       WorkspaceRMI workspace = new WorkspaceRMI(id, 0, user, false, false);
       globalDAO.add(workspace);
   }

   public WorkspaceRMI getWorkspace(UUID id) throws RemoteException {
      return globalDAO.getById(id);
   }

   public List<CommitInfo> doCommit(UserRMI user, WorkspaceRMI workspace,
         DeviceRMI device, List<ItemMetadataRMI> items) throws DAOException {
      return globalDAO.doCommit(user, workspace, device, items);
   }

   public WorkspaceRMI doShareFolder(UserRMI user, List<String> emails, ItemRMI item,
         boolean isEncrypted) throws ShareProposalNotCreatedException,
         UserNotFoundException {

        /*// Check the owner
         try {
         user = userDao.findById(user.getId());
         } catch (Exception e) {
         logger.error(e);
         throw new ShareProposalNotCreatedException(e);
         }

         // Get folder metadata
         try {
         item = itemDao.findById(item.getId());
         } catch (Exception e) {
         logger.error(e);
         throw new ShareProposalNotCreatedException(e);
         }

         if (item == null || !item.isFolder()) {
         throw new ShareProposalNotCreatedException(
         "No folder found with the given ID.");
         }

         // Get the source workspace
         WorkspaceRMI sourceWorkspace;
         try {
         sourceWorkspace = workspaceDAO.getById(item.getWorkspace().getId());
         } catch (Exception e) {
         logger.error(e);
         throw new ShareProposalNotCreatedException(e);
         }
         if (sourceWorkspace == null) {
         throw new ShareProposalNotCreatedException("Workspace not found.");
         }

         // Check the addressees
         List<UserRMI> addressees = new ArrayList<UserRMI>();
         for (String email : emails) {
         UserRMI addressee;
         try {
         addressee = userDao.getByEmail(email);
         if (!addressee.getId().equals(user.getId())) {
         addressees.add(addressee);
         }

         } catch (IllegalArgumentException e) {
         logger.error(e);
         throw new ShareProposalNotCreatedException(e);
         } catch (Exception e) {
         logger.warn(
         String.format(
         "Email '%s' does not correspond with any user. ",
         email), e);
         }
         }

         if (addressees.isEmpty()) {
         throw new ShareProposalNotCreatedException("No addressees found");
         }

         WorkspaceRMI workspace;

         if (sourceWorkspace.isShared()) {
         workspace = sourceWorkspace;

         } else {
         // Create the new workspace
         String container = UUID.randomUUID().toString();

         workspace = new WorkspaceRMI();
         workspace.setShared(true);
         workspace.setEncrypted(isEncrypted);
         workspace.setName(item.getFilename());
         workspace.setOwner(user.getId());
         List<UUID> users = new ArrayList<UUID>();
         for (UserRMI userInList : addressees) {
         users.add(userInList.getId());
         }
         workspace.setUsers(users);
         workspace.setSwiftContainer(container);
         workspace.setSwiftUrl(Config.getSwiftUrl() + "/"
         + user.getSwiftAccount());

         // Create container in Swift
         try {
         storageManager.createNewWorkspace(workspace);
         }a catch (Exception e) {
         logger.error(e);
         throw new ShareProposalNotCreatedException(e);
         }

         // Save the workspace to the DB
         try {
         workspaceDAO.add(workspace);
         // add the owner to the workspace
         workspaceDAO.addUser(user, workspace);

         } catch (Exception e) {
         logger.error(e);
         throw new ShareProposalNotCreatedException(e);
         }

         // Grant user to container in Swift
         try {
         storageManager.grantUserToWorkspace(user, user, workspace);
         } catch (Exception e) {
         logger.error(e);
         throw new ShareProposalNotCreatedException(e);
         }

         // Migrate files to new workspace
         List<String> chunks;
         try {
         chunks = itemDao.migrateItem(item.getId(), workspace.getId());
         } catch (Exception e) {
         logger.error(e);
         throw new ShareProposalNotCreatedException(e);
         }

         // Move chunks to new container
         for (String chunkName : chunks) {
         try {
         storageManager.copyChunk(sourceWorkspace, workspace,
         chunkName);
         } catch (Exception e) {
         logger.error(e);
         throw new ShareProposalNotCreatedException(e);
         }
         }
         }

         // Add the addressees to the workspace
         for (UserRMI addressee : addressees) {
         try {
         workspaceDAO.addUser(addressee, workspace);

         } catch (Exception e) {
         workspace.getUsers().remove(addressee);
         logger.error(
         String.format(
         "An error ocurred when adding the user '%s' to workspace '%s'",
         addressee.getId(), workspace.getId()), e);
         }

         // Grant the user to container in Swift
         try {
         storageManager.grantUserToWorkspace(user, addressee, workspace);
         } catch (Exception e) {
         logger.error(e);
         throw new ShareProposalNotCreatedException(e);
         }
         }

         return workspace;*/
      return null;
   }

   public UnshareData doUnshareFolder(UserRMI user, List<String> emails, ItemRMI item, boolean isEncrypted)
         throws ShareProposalNotCreatedException, UserNotFoundException {

        /*UnshareData response;
         // Check the owner
         try {
         user = userDao.findById(user.getId());
         } catch (Exception e) {
         logger.error(e);
         throw new ShareProposalNotCreatedException(e);
         }

         // Get folder metadata
         try {
         item = itemDao.findById(item.getId());
         } catch (Exception e) {
         logger.error(e);
         throw new ShareProposalNotCreatedException(e);
         }

         if (item == null || !item.isFolder()) {
         throw new ShareProposalNotCreatedException("No folder found with the given ID.");
         }

         // Get the workspace
         WorkspaceRMI sourceWorkspace;
         try {
         sourceWorkspace = workspaceDAO.getById(item.getWorkspace().getId());
         } catch (Exception e) {
         logger.error(e);
         throw new ShareProposalNotCreatedException(e);
         }
         if (sourceWorkspace == null) {
         throw new ShareProposalNotCreatedException("Workspace not found.");
         }
         if (!sourceWorkspace.isShared()) {
         throw new ShareProposalNotCreatedException("This workspace is not shared.");
         }
		
         // Check the addressees
         List<UserRMI> addressees = new ArrayList<UserRMI>();
         for (String email : emails) {
         UserRMI addressee;
         try {
         addressee = userDao.getByEmail(email);
         if (addressee.getId().equals(sourceWorkspace.getOwner())){
         logger.warn(String.format("Email '%s' corresponds with owner of the folder. ", email));
         throw new ShareProposalNotCreatedException("Email "+email+" corresponds with owner of the folder.");
				
         }
				
         if (!addressee.getId().equals(user.getId())) {
         addressees.add(addressee);
         }
				

         } catch (IllegalArgumentException e) {
         logger.error(e);
         throw new ShareProposalNotCreatedException(e);
         } catch (Exception e) {
         logger.warn(String.format("Email '%s' does not correspond with any user. ", email), e);
         }
         }

         if (addressees.isEmpty()) {
         throw new ShareProposalNotCreatedException("No addressees found");
         }

         // get workspace members
         List<UserWorkspaceRMI> workspaceMembers;
         try {
         workspaceMembers = doGetWorkspaceMembers(user, sourceWorkspace);
         } catch (InternalServerError e1) {
         throw new ShareProposalNotCreatedException(e1.toString());
         }

         // remove users from workspace
         List<UserRMI> usersToRemove = new ArrayList<UserRMI>();
		
         for (UserRMI userToRemove : addressees) {
         for (UserWorkspaceRMI member : workspaceMembers) {
         if (member.getUser().getEmail().equals(userToRemove.getEmail())) {
         workspaceMembers.remove(member);
         usersToRemove.add(userToRemove);
         break;
         }
         }
         }

         if (workspaceMembers.size() <= 1) {
         // All members have been removed from the workspace
         WorkspaceRMI defaultWorkspace;
         try {
         //Always the last member of a shared folder should be the owner
         defaultWorkspace = workspaceDAO.getDefaultWorkspaceByUserId(sourceWorkspace.getOwner());
         } catch (Exception e) {
         logger.error(e);
         throw new ShareProposalNotCreatedException("Could not get default workspace");
         }

         // Migrate files to new workspace
         List<String> chunks;
         try {
         chunks = itemDao.migrateItem(item.getId(), defaultWorkspace.getId());
         } catch (Exception e) {
         logger.error(e);
         throw new ShareProposalNotCreatedException(e);
         }

         // Move chunks to new container
         for (String chunkName : chunks) {
         try {
         storageManager.copyChunk(sourceWorkspace, defaultWorkspace, chunkName);
         } catch (Exception e) {
         logger.error(e);
         throw new ShareProposalNotCreatedException(e);
         }
         }
			
         // delete workspace
         try {
         workspaceDAO.deleteWorkspace(sourceWorkspace.getId());
         } catch (Exception e) {
         logger.error(e);
         throw new ShareProposalNotCreatedException(e);
         }
			
         // delete container from swift
         try {
         storageManager.deleteWorkspace(sourceWorkspace);
         } catch (Exception e) {
         logger.error(e);
         throw new ShareProposalNotCreatedException(e);
         }
			
         response = new UnshareData(usersToRemove, sourceWorkspace, true);

         } else {
			
         for(UserRMI userToRemove : usersToRemove){
				
         try {
         workspaceDAO.deleteUser(userToRemove, sourceWorkspace);
         } catch (Exception e) {
         logger.error(e);
         throw new ShareProposalNotCreatedException(e);
         }
				
         try {
         storageManager.removeUserToWorkspace(user, userToRemove, sourceWorkspace);
         } catch (Exception e) {
         logger.error(e);
         throw new ShareProposalNotCreatedException(e);
         }
         }
         response = new UnshareData(usersToRemove, sourceWorkspace, false);

         }
         return response;*/
      return null;
   }

   public List<UserRMI> doGetWorkspaceMembers(UserRMI user,
         WorkspaceRMI workspace) throws InternalServerError {

      // TODO: check user permissions.

      List<UserRMI> members;
      try {
         members = globalDAO.getMembersById(workspace.getId());
      } catch (Exception e) {
         logger.error(e);
         throw new InternalServerError(e);
      }

      if (members == null || members.isEmpty()) {
         throw new InternalServerError("No members found in workspace.");
      }

      return members;
   }

   public Connection getConnection() {
      return this.connection;
   }

}
