package me.bannockhost.bannockhost.invite;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "invites")
public class InviteModel {

    /**
     * Constructor for InviteModel
     * @param ownerId The ID of the owner of the invite
     * @param invite The invite code itself
     */
    public InviteModel(long ownerId, String invite) {
        this.ownerId = ownerId;
        this.invite = invite;
    }

    public InviteModel(){}

    @Column(name = "id", unique = true)
    @GeneratedValue
    @Id
    private long inviteId;

    @Column(name = "ownerId")
    private long ownerId;

    @Column(name = "invite", unique = true)
    private String invite;

    public long getOwnerId() {
        return ownerId;
    }

    public String getInvite() {
        return invite;
    }

    public void setInvite(String invite) {
        this.invite = invite;
    }
}
