// Create an in memory JanusGraph instance for bAmpli.
// Load bAmpli Graph Schema, including index and P&Q Factory data.
// To be loaded and run inside the Gremlin Console from JanusGraph.
// Usage:
//   :load bampli.groovy

println "\n=======================================";[]
println "Creating in-memory bAmpli Graph";[]
println "=======================================\n";[]
// Create a new graph instance
graph = JanusGraphFactory.open('inmemory')
mgmt = graph.openManagement()

println "\n=================================";[]
println "Defining labels";[]
println "=================================\n";[]

// Vertex labels
Version = mgmt.makeVertexLabel('Version').make()
Product = mgmt.makeVertexLabel('Product').make()
Wip = mgmt.makeVertexLabel('Wip').make()
Stage = mgmt.makeVertexLabel('Stage').make()
Task = mgmt.makeVertexLabel('Task').make()

// Edge labels and usage
ROUTE = mgmt.makeEdgeLabel('ROUTE').multiplicity(MULTI).make()
TRANSF = mgmt.makeEdgeLabel('TRANSF').multiplicity(MULTI).make()
DEPLOY = mgmt.makeEdgeLabel('DEPLOY').multiplicity(SIMPLE).make()
GET_WIP = mgmt.makeEdgeLabel('GET_WIP').multiplicity(SIMPLE).make()
PUT_WIP = mgmt.makeEdgeLabel('PUT_WIP').multiplicity(SIMPLE).make()

// mgmt.commit()
// mgmt = graph.openManagement()

println "\n=================================";[]
println "Creating property keys";[]
println "=================================\n";[]

// Vertex
p_product = mgmt.makePropertyKey('product').dataType(String.class).cardinality(Cardinality.SINGLE).make()
p_wip = mgmt.makePropertyKey('wip').dataType(String.class).cardinality(Cardinality.SINGLE).make()
p_stage = mgmt.makePropertyKey('stage').dataType(String.class).cardinality(Cardinality.SINGLE).make()
p_task = mgmt.makePropertyKey('task').dataType(String.class).cardinality(Cardinality.SINGLE).make()
p_desc = mgmt.makePropertyKey('desc').dataType(String.class).cardinality(Cardinality.SINGLE).make()

// Edge
p_elapsed = mgmt.makePropertyKey('elapsed').dataType(Integer.class).cardinality(Cardinality.SINGLE).make()
p_route = mgmt.makePropertyKey('route').dataType(String.class).cardinality(Cardinality.SINGLE).make()
p_transf = mgmt.makePropertyKey('transf').dataType(String.class).cardinality(Cardinality.SINGLE).make()
p_deploy = mgmt.makePropertyKey('deploy').dataType(String.class).cardinality(Cardinality.SINGLE).make()
p_get_wip = mgmt.makePropertyKey('get_wip').dataType(String.class).cardinality(Cardinality.SINGLE).make()
p_put_wip = mgmt.makePropertyKey('put_wip').dataType(String.class).cardinality(Cardinality.SINGLE).make()

// println "\n=================================";[]
// println "Define Connections";[]
// println "=================================\n";[]

// mgmt.addConnection(ROUTE, Stage, Stage)
// mgmt.addConnection(ROUTE, Product, Stage)
// mgmt.addConnection(ROUTE, Stage, Product)
// mgmt.addConnection(DEPLOY, Stage, Task)
// mgmt.addConnection(DEPLOY, Product, Task)
// mgmt.addConnection(TRANSF, Wip, Wip)
// mgmt.addConnection(GET_WIP, Task, Wip)
// mgmt.addConnection(PUT_WIP, Task, Wip)

mgmt.commit()

println "\n=================================";[]
println "Building index";[]
println "=================================\n";[]

// Construct (unique?) composite index for commonly used property keys
graph.tx().rollback()
mgmt=graph.openManagement()

p_product = mgmt.getPropertyKey('product')
p_wip = mgmt.getPropertyKey('wip')
p_stage = mgmt.getPropertyKey('stage')
p_task = mgmt.getPropertyKey('task')
p_desc = mgmt.getPropertyKey('desc')
p_elapsed = mgmt.getPropertyKey('elapsed')
p_route = mgmt.getPropertyKey('route')
p_transf = mgmt.getPropertyKey('transf')
p_deploy = mgmt.getPropertyKey('deploy')
p_get_wip = mgmt.getPropertyKey('get_wip')
p_put_wip = mgmt.getPropertyKey('put_wip')

idx1 = mgmt.buildIndex('prodIndex', Vertex.class).addKey(p_product).buildCompositeIndex()
idx2 = mgmt.buildIndex('wipxIndex', Vertex.class).addKey(p_wip).buildCompositeIndex()
idx3 = mgmt.buildIndex('stagIndex', Vertex.class).addKey(p_stage).buildCompositeIndex()
idx4 = mgmt.buildIndex('taskIndex', Vertex.class).addKey(p_task).buildCompositeIndex()
idx5 = mgmt.buildIndex('elapIndex', Edge.class).addKey(p_elapsed).buildCompositeIndex()
idx6 = mgmt.buildIndex('routIndex', Edge.class).addKey(p_route).buildCompositeIndex()
idx7 = mgmt.buildIndex('tranIndex', Edge.class).addKey(p_transf).buildCompositeIndex()
idx8 = mgmt.buildIndex('deplIndex', Edge.class).addKey(p_deploy).buildCompositeIndex()
idx9 = mgmt.buildIndex('getwIndex', Edge.class).addKey(p_get_wip).buildCompositeIndex()
idxA = mgmt.buildIndex('putwIndex', Edge.class).addKey(p_put_wip).buildCompositeIndex()

//idx1 = mgmt.buildIndex('prodIndex', Vertex.class).addKey(prod).unique().buildCompositeIndex()
// mgmt.addProperties(Product, p_desc)
// mgmt.addProperties(Stage, p_desc)
// mgmt.addProperties(TRANSF, p_elapsed)
// mgmt.addProperties(GET_WIP, p_elapsed)
// mgmt.addProperties(PUT_WIP, p_elapsed)

mgmt.commit()

println "\n=================================";[]
println "Waiting for prodIndex to be ready";[]
mgmt=graph.openManagement()
mgmt.awaitGraphIndexStatus(graph, 'prodIndex').status(SchemaStatus.REGISTERED).call()
mgmt.commit()

println "Waiting for wipxIndex to be ready";[]
mgmt=graph.openManagement()
mgmt.awaitGraphIndexStatus(graph, 'wipxIndex').status(SchemaStatus.REGISTERED).call()
mgmt.commit()

println "Waiting for stagIndex to be ready";[]
mgmt=graph.openManagement()
mgmt.awaitGraphIndexStatus(graph, 'stagIndex').status(SchemaStatus.REGISTERED).call()
mgmt.commit()

println "Waiting for taskIndex to be ready";[]
mgmt=graph.openManagement()
mgmt.awaitGraphIndexStatus(graph, 'taskIndex').status(SchemaStatus.REGISTERED).call()
mgmt.commit()

println "Waiting for elapIndex to be ready";[]
mgmt=graph.openManagement()
mgmt.awaitGraphIndexStatus(graph, 'elapIndex').status(SchemaStatus.REGISTERED).call()
mgmt.commit()

println "Waiting for routIndex to be ready";[]
mgmt=graph.openManagement()
mgmt.awaitGraphIndexStatus(graph, 'routIndex').status(SchemaStatus.REGISTERED).call()
mgmt.commit()

println "Waiting for tranIndex to be ready";[]
mgmt=graph.openManagement()
mgmt.awaitGraphIndexStatus(graph, 'tranIndex').status(SchemaStatus.REGISTERED).call()
mgmt.commit()

println "Waiting for deplIndex to be ready";[]
mgmt=graph.openManagement()
mgmt.awaitGraphIndexStatus(graph, 'deplIndex').status(SchemaStatus.REGISTERED).call()
mgmt.commit()

println "Waiting for getwIndex to be ready";[]
mgmt=graph.openManagement()
mgmt.awaitGraphIndexStatus(graph, 'getwIndex').status(SchemaStatus.REGISTERED).call()
mgmt.commit()

println "Waiting for putwIndex to be ready";[]
mgmt=graph.openManagement()
mgmt.awaitGraphIndexStatus(graph, 'putwIndex').status(SchemaStatus.REGISTERED).call()
mgmt.commit()
println "=================================\n";[]

println "\n=================================";[]
println "Re-indexing";[]
println "=================================\n";[]
mgmt=graph.openManagement()

mgmt.awaitGraphIndexStatus(graph, 'prodIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('prodIndex'), SchemaAction.REINDEX).get()

mgmt.awaitGraphIndexStatus(graph, 'wipxIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('wipxIndex'), SchemaAction.REINDEX).get()

mgmt.awaitGraphIndexStatus(graph, 'stagIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('stagIndex'), SchemaAction.REINDEX).get()

mgmt.awaitGraphIndexStatus(graph, 'taskIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('taskIndex'), SchemaAction.REINDEX).get()

mgmt.awaitGraphIndexStatus(graph, 'elapIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('elapIndex'), SchemaAction.REINDEX).get()

mgmt.awaitGraphIndexStatus(graph, 'routIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('routIndex'), SchemaAction.REINDEX).get()

mgmt.awaitGraphIndexStatus(graph, 'tranIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('tranIndex'), SchemaAction.REINDEX).get()

mgmt.awaitGraphIndexStatus(graph, 'deplIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('deplIndex'), SchemaAction.REINDEX).get()

mgmt.awaitGraphIndexStatus(graph, 'getwIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('getwIndex'), SchemaAction.REINDEX).get()

mgmt.awaitGraphIndexStatus(graph, 'putwIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('putwIndex'), SchemaAction.REINDEX).get()

mgmt.commit()

// // Load the air-routes graph and display a few statistics.
// // Not all of these steps use the index so Janus Graph will give us some warnings.
// println "\n========================";[]
// println "Loading air-routes graph";[]
// println "=================================\n";[]
// graph.io(graphml()).readGraph('/bampli/gremlin/air-routes.graphml')
// graph.tx().commit();[]

// Setup our traversal source object
g = graph.traversal()

// // Display a few statistics
// apt = g.V().has('type','airport').count().next();[]
// cty = g.V().has('type','country').count().next();[]
// cnt = g.V().has('type','continent').count().next();[]
// rts = g.E().hasLabel('route').count().next();[]
// edg = g.E().count().next();[]

// println "Airports   : $apt";[]
// println "Countries  : $cty";[]
// println "Continents : $cnt";[]
// println "Routes     : $rts";[]
// println "Edges      : $edg";[]

// Look at the properties, just as an exampl of how to do it!
println "\n=================================";[]
println "Retrieving property keys";[]
println "=================================\n";[]
mgmt = graph.openManagement()
types = mgmt.getRelationTypes(PropertyKey.class);[] 
types.each{println "$it\t: " + mgmt.getPropertyKey("$it").dataType() + " " + mgmt.getPropertyKey("$it").cardinality()};[]
mgmt.commit()   
