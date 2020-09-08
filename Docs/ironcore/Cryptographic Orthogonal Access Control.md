![](https://img.shields.io/badge/github-xugaoyi-brightgreen.svg)
![](https://github.com/wbwangk/wbwangk.github.io/blob/master/Docs/ironcore/guide-Encrypt%20Directly%20To%20Users.svg)
# Cryptographic Orthogonal Access Control

> All problems in computer science can be solved by another level of indirection.
>
> \- David Wheeler

With orthogonal access control, your decision of who to encrypt to is separate from your choice of who can decrypt. You abstract classes of users and services into groups and then encrypt data to the group. At any point in time, you specify who is currently a member of the group. Only the group members’ private keys can unlock data encrypted to the group.

​                  ![Image showing admin adding/removing from group](https://d33wubrfki0l68.cloudfront.net/b5d921b2a1318b97c1deab938b572cc7aa020416/18ae1/static/c0b0333d0ea594923dcd3166e7377674/38b44/orthogonal-access-control-add-remove-members.jpg)            

### You encrypt data to a group.

​                  ![Image showing file encrypting to group](https://d33wubrfki0l68.cloudfront.net/4848dfa44c0012cb8b29a9d344debd8a73062d2e/c6445/static/d4ee9807e105b9935155872209c95c69/38b44/orthogonal-access-control-file-to-group.jpg)            

You can add or remove group members at any time, without changing encrypted data. Groups can be any size, even millions of users, and adding and removing members are constant time operations regardless of how many documents or users there are. There is no need for shared secrets or trusted servers.

### Example

The Starship Enterprise has a sickbay to provide healthcare services to crewmembers. Starfleet regulations require that any personal health information (PHI) be restricted only to onboard medical personnel. The Starship Enterprise team uses transform encryption as follows:

**Step 1**
 Dr. McCoy creates a PHI-Readers cryptographic group.

**Step 2**
 Dr. McCoy encrypts PHI and intake records to the group.

**Step 3**
 Dr. McCoy adds Nurse Chapel as a member of the PHI-Readers group.

**Step 4**
 Various biomedical scanners read and create PHI records. These scanners detect blood anomalies, look for viruses, etc. Dr. McCoy adds the service accounts for the scanners as members of the PHI-Readers group.

**Step 5**
 As medical staff join and leave, group membership is modified. The decision of what data to decrypt is separate from the choice of who can decrypt.
