package com.tang.platform.consistency.jraft.cluster;

import com.tang.platform.consistency.jraft.meta.node.NodeEndpoint;
import com.tang.platform.consistency.jraft.meta.node.NodeMeta;
import com.tang.platform.consistency.jraft.meta.node.NodeId;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NodeGroup {
    private final Map<NodeId, ClusterMember> memberMap;

    public NodeGroup(Collection<NodeMeta> nodeMetas) {
        this.memberMap = buildMemberMap(nodeMetas);
    }

    private Map<NodeId, ClusterMember> buildMemberMap(Collection<NodeMeta> nodeMetas) {
        Map<NodeId, ClusterMember> map = new HashMap<>();
        nodeMetas.forEach(n -> {
            map.put(n.getId(), new ClusterMember(n));
        });
        return map;
    }

    public List<NodeEndpoint> fetchOtherNodeEndpoints(NodeId selfId) {
        return memberMap.entrySet().stream().filter(m -> m.getKey().equals(selfId)).map(Map.Entry::getValue).map(s -> s.getNodeMeta().getEndpoint()).collect(Collectors.toList());
    }

    public List<ClusterMember> fetchOtherClusterMember(NodeId selfId){
        return memberMap.entrySet().stream().filter(m -> m.getKey().equals(selfId)).map(Map.Entry::getValue).collect(Collectors.toList());

    }

    public ClusterMember findMember(NodeId id) {
        if (!memberMap.containsKey(id)) {
            throw new IllegalArgumentException("no such cluster member " + id);

        }
        return memberMap.get(id);
    }

    public int getNodeSize() {
        return memberMap.size();
    }

    public ClusterMember fetchMember(NodeId id) {
        return memberMap.get(id);
    }
    public int getMatchIndexOfMajor(){
        //todo
        return 0;
    }
}
